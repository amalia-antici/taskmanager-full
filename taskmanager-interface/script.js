const API_URL = "http://localhost:8080/tasks";

window.onload=function()
{
    loadTasks();
    loadStats();
    document.getElementById("addTaskButton").addEventListener("click", addTask);

}

function loadTasks()
{
    fetch(API_URL+ "?page=0&size=10").then(response=>response.json()).then(data=>{
        const tasks=data.content;
        const taskContainer=document.getElementById("tasks_container");
        const completedContainer=document.getElementById("completed_container");

        taskContainer.innerHTML="";
        completedContainer.innerHTML="";

        tasks.forEach(task => {
            const element=createTaskElement(task);
            if(task.status==="DONE")
                completedContainer.prepend(element);
            else
                taskContainer.appendChild(element);
        });
    })
}

function addTask()
{
    const title=document.getElementById("title").value;
    const description=document.getElementById("description").value;
    const dueDate=document.getElementById("dueDate").value;

    const task={
        title: title,
        description: description || "",
        dueDate:dueDate,
        status: "TODO"
    };

    fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(task)
                    }).then(()=>{clearForm(); loadTasks(); loadStats()});
}


function clearForm()
{
    document.getElementById("title").value="";
    document.getElementById("description").value="";
    document.getElementById("dueDate").value="";
}

function deleteTask(id)
{
    fetch(API_URL+"/"+id, {method: "DELETE"}).then(()=>{loadTasks(); loadStats();});
}

function updateStatus(task, newStatus)
{
    task.status=newStatus;
    fetch(API_URL+"/"+task.id, {
        method:"PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(task)
    }).then(()=>{loadTasks(); loadStats();});
}

function formatRelativeDate(isoString)
{
    if (!isoString)
        return "No due date";
    const target=new Date(isoString);
    const now=new Date();

    const diffInMs=target-now;
    const diffInDays=Math.round(diffInMs/ (1000* 60 * 60 * 24));
    if (diffInDays===0) return "Due today";
    if (diffInDays===1) return "Due tomorrow";
    if(diffInDays===-1) return "Yesterday (Overdue!)";

    if (diffInDays>1) return `Due in ${diffInDays} days / ${isoString}`;
    if (diffInDays<-1) return `${Math.abs(diffInDays)} days overdue / / ${isoString}`;

    return target.toLocaleDateString();
}


function createTaskElement(task)
{
    const taskItem=document.createElement("div");
    taskItem.className="task_item";

    const header=document.createElement("div");
    header.className="task_header";

    const statusSelect=document.createElement("select");
    statusSelect.className="status_select";

    statusSelect.innerHTML=`<option value="TODO">TODO</option>
                            <option value="IN_PROGRESS">IN_PROGRESS</option>
                            <option value="DONE">DONE</option>`;
    statusSelect.value=task.status;

    const title=document.createElement("span");
    title.className="task_title";
    title.innerText=task.title;
    
    const deleteButton=document.createElement("button");
    deleteButton.className="delete_button";
    deleteButton.innerText="DELETE";

    header.appendChild(statusSelect);
    header.appendChild(title);
    header.appendChild(deleteButton);

    const details=document.createElement("div");
    details.className="task_details";
    details.style.display="none";
    details.innerHTML= `Description: ${task.description || ""}<br>
                        Due: ${formatRelativeDate(task.dueDate)}
                        `;

    taskItem.appendChild(header);
    taskItem.appendChild(details);

    header.addEventListener("click", function()
{
    details.style.display=details.style.display==="none"? "block":"none"
});

    deleteButton.addEventListener("click", function(e) {
        e.stopPropagation();
        deleteTask(task.id);
    });

    statusSelect.addEventListener("change", function(e)
{
    e.stopPropagation();
    updateStatus(task, statusSelect.value);
})

    return taskItem;
}

function searchTask()
{
    const searchValue=document.getElementById("search_bar").value;
    fetch(API_URL+"/search?title="+searchValue).then(response=>response.json()).then(data=>
        {
            const tasks=data.content;
           const taskContainer=document.getElementById("tasks_container");
           const completedContainer=document.getElementById("completed_container");
           taskContainer.innerHTML="";
           completedContainer.innerHTML="";

           tasks.forEach(task=>
            {
                const element=createTaskElement(task);
                if (task.status==="DONE")
                    completedContainer.prepend(element);
                else
                    taskContainer.appendChild(element);
            }
           )

        }
    )
}

function loadStats()
{
    fetch(API_URL+"/stats").then(response=>response.json()).then(stats=>
    {
        document.getElementById("totalTasks").innerText="Total Tasks: "+stats.total;
        
        const xArray=["TODO", "IN PROGRESS", "DONE"];
        const yArray=[stats.todo, stats.inProgress, stats.done];
        const data=[{
            x: xArray,
            y: yArray,
            type:"bar",
            marker:{
                color: ["#e1ce7a", "#fdd692", "#ec7357"]
            }
        }];
        const layout={
            xaxis: {title: "Status"},
            yaxis: {title: "Number of Tasks"},
            paper_bgcolor: 'rgba(0,0,0,0)',
            plot_bgcolor: 'rgba(0,0,0,0)'
        };

        Plotly.newPlot("statsChart", data, layout);
    });
}