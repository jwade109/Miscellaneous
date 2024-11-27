#!/usr/bin/env python

"""Echo server using the asyncio API."""

import asyncio
from websockets.asyncio.server import serve
import time
import json


def make_server_packet():
    return dict(
        time=time.time(),
    )

async def handler(conn):
    msg = await conn.recv()
    while True:
        p = make_server_packet()
        await conn.send(json.dumps(p))
        time.sleep(0.1)


async def main():
    async with serve(handler, "localhost", 8080) as server:
        await server.serve_forever()


if __name__ == "__main__":
    asyncio.run(main())
