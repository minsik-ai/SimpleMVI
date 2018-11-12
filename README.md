# SimpleMVI
[ ![Download](https://api.bintray.com/packages/trent-dev/com.trent.simplemvi/simplemvi/images/download.svg) ](https://bintray.com/trent-dev/com.trent.simplemvi/simplemvi/_latestVersion)

Lightweight Android MVI library for uni-directional architecture.

The goal is to provide a set of APIs and Classes that can conveniently setup a complex screen with many different states in a highly structured manner.

## What is MVI?

MVI is a uni-directional architecture for Android.

This library takes a simplified & practical approach for configuring MVI for real-world cases.

```
          Intent                       Result                    ViewState
 MviView --------> MviProcessorHolder --------> MviReducerHolder ----------> MviView
             |                                                        |
             ---------------------- MviViewModel ----------------------
```

This video explains it well : https://www.youtube.com/watch?v=64rQ9GKphTg

## Setup

### Gradle

From jcenter :

```Groovy
implementation 'com.trent.simplemvi:simplemvi:0.9.2'
```

RxJava 2 is required.

## How to use

### Initial Setup

Let's say we want to create a screen with name `Main`.

Start by creating `sealed class` for each of the required components, `Intent`, `Result` and `ViewState`.

```Kotlin
sealed class MainIntent : MviIntent

sealed class MainResult : MviResult

sealed class MainViewState : MviViewState
```

Also create other classes that implement `MviProcessorHolder`, `MviReducerHolder`.

```Kotlin
class MainProcessorHolder(model: MainModel) : MviProcessorHolder<MainIntent, MainResult>

class MainReducerHolder : MviReducerHolder<MainResult, MainViewState>
```

Create a `ViewModel` & `View` as well.

```Kotlin
class MainViewModel(
  processorHolder: MviProcessorHolder<MainIntent, MainResult>,
  reducerHolder: MviReducerHolder<MainResult, MainViewState>
  ) : MviViewModel<MainIntent, MainResult, MainViewState>(processorHolder, reducerHolder)

class MainView(
  containerView: View, viewModel: MainViewModel
  ) : MviView<MainIntent, MainResult, MainViewState>(viewModel)
```

In your Activity or Fragment, initialize at Activity `onCreate()` with code similar to this.

```Kotlin
viewModel = ViewModelProviders.of(
  this,
  MviViewModelFactory(
    MainProcessorHolder(MainModel()),
    MainReducerHolder()
  )
).get(MainViewModel::class.java)

MainView(this.view, viewModel).apply {
  lifecycle.addObserver(this)
  setup()
}
```

You may need more methods & parameters for your classes depending on how you initialize your View references etc.

### Configuration Workflow

Whichever way you go about configuring MVI is fine, I usually go back and forth, but generally I do this.
I recommend you to find your workflow as well.

1. Draw XML layouts.
2. Sketch possible states of the view with `ViewState`.
3. Configure `render()` method of `MainView` such that view is drawn correctly according to `ViewState`.
4. Sketch possible actions that user will take to change the view with `Intent`.
5. Configure callbacks such as `View.setOnClickListener()` correctly such that user actions will trigger correct `Intent` to `intentsSubject`, passing it to `MainProcessorHolder`.
6. Implement `Result` & `MainProcessorHolder` such that `Intent` can produce correct `Result`. Handle side-effects such as DB & Networking while processing intents as well. This result will be passed to `MainReducerHolder`.
7. Implement `MainReducerHolder` such that `Result` will produce correct `ViewState` in combination with previous `ViewState`. This resulting view state will be passed to `MainView`, and the new view state will be rendered.

After initial configuration, you will find that modification is a breeze.

See `sampleapp` module for detailed recommended usage.
