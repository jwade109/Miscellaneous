use bevy::color::palettes::css::*;
use bevy::input::keyboard::KeyboardInput;
use bevy::prelude::*;
use bevy::time::common_conditions::on_timer;
use bevy::ui::RelativeCursorPosition;
use bevy_renet::netcode::*;
use bevy_renet::*;
use renet::*;
use std::net::UdpSocket;
use std::time::SystemTime;

use chatroom::client::ui::*;
use chatroom::common::messages::*;

fn main() {
    let username = std::env::args().nth(1).unwrap();

    let mut app = App::new();
    app.add_plugins(RenetClientPlugin);

    let client = RenetClient::new(ConnectionConfig::default());
    app.insert_resource(client);

    // Setup the transport layer
    app.add_plugins(NetcodeClientPlugin);

    let server_addr = "127.0.0.1:5000".parse().unwrap();

    let current_time = SystemTime::now()
        .duration_since(SystemTime::UNIX_EPOCH)
        .unwrap();
    let client_id = current_time.as_millis() as u64;

    let authentication = ClientAuthentication::Unsecure {
        server_addr,
        client_id,
        user_data: None,
        protocol_id: 0,
    };
    let socket = UdpSocket::bind("127.0.0.1:0").unwrap();
    let current_time = SystemTime::now()
        .duration_since(SystemTime::UNIX_EPOCH)
        .unwrap();

    let transport = NetcodeClientTransport::new(current_time, authentication, socket).unwrap();

    app.insert_resource(transport);

    app.add_systems(Startup, init_client);

    app.add_systems(
        Update,
        (
            receive_message_system,
            update_ui_on_receive_message,
            only_show_latest_20_messages,
            // update_text_input_active_status,
            on_key_press,
            update_input_text_system,
        )
            .chain(),
    );

    app.insert_resource(TextInput::default());
    app.insert_resource(Username(username));

    app.add_plugins(DefaultPlugins);

    app.add_message::<ServerMessage>();

    app.run();
}

// Systems

fn init_client(mut commands: Commands) {
    commands.spawn(Camera2d);
    commands.insert_resource(ClearColor(GRAY.into()));
    let ui = make_client_ui();
    commands.spawn(ui);
}

fn receive_message_system(
    mut client: ResMut<RenetClient>,
    mut writer: MessageWriter<ServerMessage>,
) {
    while let Some(message) = client.receive_message(DefaultChannel::ReliableOrdered) {
        if let Ok(msg) = ServerMessage::from_bytes(message) {
            info!("Got message: {:?}", &msg);
            writer.write(msg);
        }
    }
}

/// inserts new UI elements into the tree when ServerMessage events are
/// received from any other system
fn update_ui_on_receive_message(
    mut commands: Commands,
    ui_root: Single<Entity, With<ChatWindowMarker>>,
    mut messages: MessageReader<ServerMessage>,
) {
    for msg in messages.read() {
        let s = match &msg.contents {
            ServerMessageContents::UserMessage { username, contents } => {
                format!("[{}] {}", username, contents)
            }
            ServerMessageContents::UserJoined { id } => {
                format!("--- User {} just connected ---", id)
            }
            ServerMessageContents::UserLeft { id } => {
                format!("--- User {} just disconnected ---", id)
            }
        };
        commands
            .entity(ui_root.entity())
            .with_child(make_text_node(s));
    }
}

fn only_show_latest_20_messages(
    mut commands: Commands,
    ui_root: Single<&Children, With<ChatWindowMarker>>,
) {
    if ui_root.len() > 20 {
        for i in 0..ui_root.len() - 20 {
            let e = ui_root[i];
            commands.entity(e).despawn();
        }
    }
}

#[derive(Resource, Default)]
pub struct TextInput(pub String);

#[derive(Resource, Default)]
pub struct Username(pub String);

fn on_key_press(
    mut keys: MessageReader<KeyboardInput>,
    mut input: ResMut<TextInput>,
    username: Res<Username>,
    mut server: ResMut<RenetClient>,
) {
    use bevy::input::ButtonState;
    use bevy::input::keyboard::Key;

    for key in keys.read() {
        match (&key.logical_key, key.state) {
            (Key::Character(c), ButtonState::Pressed) => {
                input.0 += c;
            }
            (Key::Backspace, ButtonState::Pressed) => {
                input.0.pop();
            }
            (Key::Enter, ButtonState::Pressed) => {
                let msg = ServerMessage::user_message(&username.0, &input.0);
                input.0.clear();
                server.send_message(DefaultChannel::ReliableOrdered, msg.to_bytes());
            }
            (Key::Space, ButtonState::Pressed) => {
                input.0 += " ";
            }
            _ => (),
        }
    }
}

fn update_input_text_system(
    input: Res<TextInput>,
    mut text: Single<&mut Text, With<TextInputMarker>>,
) {
    text.0 = input.0.clone();
}
