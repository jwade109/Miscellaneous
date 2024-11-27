"use strict";

async function sleep(ms)
{
    await new Promise(r => setTimeout(r, ms));
}

const servercontent = document.getElementById("servercontent");
console.log(servercontent);

function on_socket_message(event)
{
    console.log("Message from server \"" + event.data + "\"");

    servercontent.innerText = event.data;
}

function socket_send(socket, message)
{
    try
    {
        socket.send("Hello!");
        return true;
    }
    catch (e)
    {
        return false;
    }
}

async function main()
{
    // Create WebSocket connection.
    const socket = new WebSocket("ws://localhost:8080");

    socket.addEventListener("open", (event) => socket.send("Hello!"));
    socket.addEventListener("message", on_socket_message);
    
    while (true)
    {
        await sleep(1000);
        // socket_send(socket, "hello");
    }    
}

main();
