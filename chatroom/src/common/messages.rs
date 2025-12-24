use bevy::prelude::*;
use bytes::Bytes;
use serde::{Deserialize, Serialize};
use uuid::Uuid;

#[derive(Message, Debug, Deserialize, Serialize)]
pub struct ServerMessage {
    pub uuid: Uuid,
    pub contents: ServerMessageContents,
}

#[derive(Debug, Deserialize, Serialize)]
pub enum ServerMessageContents {
    UserMessage { username: String, contents: String },
    UserJoined { id: u64 },
    UserLeft { id: u64 },
}

impl ServerMessage {
    pub fn user_message(uname: impl Into<String>, s: impl Into<String>) -> Self {
        let uuid = Uuid::new_v4();
        Self {
            uuid,
            contents: ServerMessageContents::UserMessage {
                username: uname.into(),
                contents: s.into(),
            },
        }
    }

    pub fn joined(id: u64) -> Self {
        let uuid = Uuid::new_v4();
        Self {
            uuid,
            contents: ServerMessageContents::UserJoined { id },
        }
    }

    pub fn left(id: u64) -> Self {
        let uuid = Uuid::new_v4();
        Self {
            uuid,
            contents: ServerMessageContents::UserLeft { id },
        }
    }

    pub fn to_bytes(self) -> Vec<u8> {
        bincode::serialize(&self).expect("Hmm. That usually works")
    }

    pub fn from_bytes(b: Bytes) -> Result<Self, Box<bincode::ErrorKind>> {
        bincode::deserialize(&b)
    }
}
