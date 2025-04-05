# 🎮 **Kotlin Homework 2024**

My project: a 2048 game built in Kotlin multiplatform. Here's what you can expect:

## 🔢 **Features:**
- **Playing on an Arbitrary-Sized Map**: Challenge yourself as much as you want.
- **Leaderboard**: Compete with yourself, reaching new heights.
- **Save GameState Persistently**: Never lose your progress! Your game state will be saved persistently.
- **Cool Animations**: Enjoy smooth and visually appealing animations to enhance your gaming experience.

## Samples

Note: The App was tested on Windows and Android!

<p align="center">
  <img src="assets\desktop_sample.PNG" width=500 height=444>
  <img src="assets\android_sample.jpg" width=200>
</p>

## Usage

- "Swipe to Slide" the tiles (Pay extra attention to the sliding animation + fade)
- Use the slider to control grid size
- Press `Refresh` to get a fresh start
- Press the `Lock button` to save the current state persistently
- HighScore is automatically updated and stored persistently

## Technologies

- moko mvvm
- kotlinx serialization
- [multiplatform-uuid](https://github.com/benasher44/uuid)
- [multiplatform settings](https://github.com/russhwolf/multiplatform-settings)
