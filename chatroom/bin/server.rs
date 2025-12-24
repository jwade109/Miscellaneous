use bevy::prelude::*;
use bevy::time::common_conditions::on_timer;
use bevy_renet::netcode::*;
use bevy_renet::*;
use renet::*;
use std::net::UdpSocket;
use std::time::SystemTime;

use chatroom::common::messages::*;

fn main() {
    let mut app = App::new();
    app.add_plugins(RenetServerPlugin);

    let server = RenetServer::new(ConnectionConfig::default());
    app.insert_resource(server);

    // Transport layer setup
    app.add_plugins(NetcodeServerPlugin);
    let server_addr = "127.0.0.1:5000".parse().unwrap();
    let socket = UdpSocket::bind(server_addr).unwrap();
    let server_config = ServerConfig {
        current_time: SystemTime::now()
            .duration_since(SystemTime::UNIX_EPOCH)
            .unwrap(),
        max_clients: 64,
        protocol_id: 0,
        public_addresses: vec![server_addr],
        authentication: ServerAuthentication::Unsecure,
    };
    let transport = NetcodeServerTransport::new(server_config, socket).unwrap();
    app.insert_resource(transport);

    app.add_systems(
        Update,
        (receive_message_system, handle_events_system).chain(),
    );

    app.add_plugins(MinimalPlugins);
    app.add_plugins(bevy::log::LogPlugin::default());

    app.run();
}

// Systems

fn receive_message_system(mut server: ResMut<RenetServer>) {
    for client_id in server.clients_id() {
        while let Some(bytes) = server.receive_message(client_id, DefaultChannel::ReliableOrdered) {
            if let Ok(msg) = ServerMessage::from_bytes(bytes) {
                info!("Got message: {:?}", &msg);
                server.broadcast_message(DefaultChannel::ReliableOrdered, msg.to_bytes());
            }
        }
    }
}

fn handle_events_system(
    mut server_events: MessageReader<ServerEvent>,
    mut server: ResMut<RenetServer>,
) {
    for event in server_events.read() {
        match event {
            ServerEvent::ClientConnected { client_id } => {
                println!("Client {client_id} connected");
                let msg = ServerMessage::joined(*client_id);
                server.broadcast_message(DefaultChannel::ReliableOrdered, msg.to_bytes());
            }
            ServerEvent::ClientDisconnected { client_id, reason } => {
                println!("Client {client_id} disconnected: {reason}");
                let msg = ServerMessage::left(*client_id);
                server.broadcast_message(DefaultChannel::ReliableOrdered, msg.to_bytes());
            }
        }
    }
}
