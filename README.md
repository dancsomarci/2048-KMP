# 🎮 **Kotlin Homework 2024**

My project: a 2048 game built in Kotlin multiplatform. Here's what you can expect:

## 🔢 **Features:**
- **Playing on an Arbitrary Sized Map**: No limitations on size, so you can challenge yourself as much as you want.
- **Leaderboard**: Compete with yourself reaching new heights on a leaderboard screen.
- **Save GameState Persistently**: Never lose your progress! Your game state will be saved persistently.
- **Cool Animations**: Enjoy smooth and visually appealing animations to enhance your gaming experience.

## Samples

Note: The App was tested on Windows and android!

<img src="assets\desktop_sample.PNG" width=500 height=444>
<img src="assets\android_sample.jpg" width=200>

## Usage

- "Swipe to Slide" the tiles (Pay extra attention to the sliding animation + fade)
- Use slider to control grid size (In reality it is not completely limited, because sadly it has to fit on the screen)
- Press `Refresh` to get a fresh start
- Press the `Lock button` to save the current state persistently
- HighScore is automatically updated and stored persistently

## Technologies

- moko mvvm
- kotlinx serialization
- [multiplatform-uuid](https://github.com/benasher44/uuid)
- [multiplatform settings](https://github.com/russhwolf/multiplatform-settings)

## Possible improvements

- DI
- Testing
- Navigation (I have experimented with Voyager, but couldn't make it work, hence there's no separated HigScore screen:(
- More work on the package structure:)

## Conclusion

I enjoyed the project and KMP as well. Definitely harder to figure out dependency related errors vs on android.

I used Fleet, which I really didn't like.

I am extremely proud of the final result, especially the animations:)


