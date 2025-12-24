use bevy::color::palettes::css::*;
use bevy::prelude::*;
use bevy::ui::RelativeCursorPosition;

#[derive(Component)]
pub struct ChatWindowMarker;

#[derive(Component)]
pub struct TextInputMarker;

pub fn input_bar_section() -> impl Bundle {
    (
        Node {
            width: percent(100.0),
            height: px(110.0),
            border: UiRect::all(px(2.0)),
            padding: UiRect::all(px(6.0)),
            ..default()
        },
        // BorderColor::all(RED),
        BackgroundColor(GRAY.into()),
        children![(
            Node {
                width: percent(100.0),
                height: percent(100.0),
                border: UiRect::all(px(2.0)),
                padding: UiRect::all(px(4.0)),
                ..default()
            },
            // BorderColor::all(RED),
            BackgroundColor(BLACK.into()),
            RelativeCursorPosition::default(),
            children![(
                TextInputMarker,
                Text::new(""),
                TextColor(WHITE.into()),
                TextShadow {
                    offset: Vec2::new(-2.0, 2.0),
                    color: Color::linear_rgba(0., 0., 0., 0.4),
                }
            )],
        )],
    )
}

pub fn chat_window_section() -> impl Bundle {
    (
        Node {
            width: percent(100.0),
            height: percent(100.0),
            flex_direction: FlexDirection::Column,
            overflow: Overflow::scroll_y(),
            ..default()
        },
        ChatWindowMarker,
    )
}

pub fn make_client_ui() -> impl Bundle {
    (
        Node {
            width: percent(100.0),
            height: percent(100.0),
            flex_direction: FlexDirection::Column,
            ..default()
        },
        children![chat_window_section(), input_bar_section(),],
    )
}

pub fn make_text_node(contents: impl Into<String>) -> impl Bundle {
    (
        Node {
            padding: UiRect::all(px(3.0)),
            margin: UiRect::vertical(px(2.0)),
            ..default()
        },
        // BackgroundColor(DARK_GRAY.into()),
        children![(
            Text::new(contents),
            TextColor(WHITE.into()),
            TextShadow {
                offset: Vec2::new(-2.0, 2.0),
                color: Color::linear_rgba(0., 0., 0., 0.4),
            }
        )],
    )
}
