# Unlockstep

you know Lockstep? from Rhythm Heaven? this is that

thanks to Squidguset for all the art for custom sprites :3

to use: 
```sh
java -jar --enable-preview <the file>
```

Since version v4.1.0 a GUI launcher is shown if no command line arguments are provided

Command line options are available:
- `--game` `--music` `--sprite` and `--color` must be either `lockstep1`, `lockstep2`, `remix6`, `remix8`, or `remix9`
- `--intro` (intro title card at start of game) can be `lockstep1`, `lockstep2`, `remix6`, `remix8`, or `remix9` or `skip` which skips it
- `--audio-delay` must be a `long` that is the amount of milliseconds it takes for your audio device to respond, having this argument skips the calibration step
- `--player-input-sound` is either `true` or `false` and forces the game to either play or not play sound effects when you do a correct input, playing sound effects when audio delay is high may cause the sound effects to be desynced and confusing
- `--perfect` triggers the `perfect` indicator
- `--auto` can be included which makes it such that the game plays itself, triggers the `auto` indicator


if you only specify some of them the rest will be determined automatically by which makes most sense

### [Download](https://github.com/Canary-Prism/Unlockstep/releases/)

I'm assuming you know how to use GitHub. If not then here:

### Download Steps

1. Click above link
2. Find latest release
3. Find "Assets" Section
4. Click the "Unlockstep-x.y.z.jar" file

### Notice

This program uses Java 22 with preview features bc i'm weird like that

#### [Here's a handy link](https://adoptium.net/temurin/releases/?version=22)

(it's for Temurin bc nobody likes oracle)


## Historical Changelog

### 4.1.0
- added lockstep endless remix
- added a GUI Launcher

### 4.0.0
- added lockstep remix 9
- added indicators `perfect` and `auto`

### 3.2.0
- added lockstep remix 8
- to accomplish this the ability to change tempo on the fly has been added
- added `--auto` parameter

### 3.1.0
- added intro for lockstep remix 6 (thanks Echo)
- changed sprites for lockstep remix 6 (thanks Echo)

### 3.0.0
- added lockstep remix 6

### 2.2.2
- fixed level 4 zoom not working properly 
- Unlockstep is now fully window size independent

### 2.2.1
- made the calibration thing look nicer
- it now no longer pretends delay of more than 175 milliseconds doesn't exist :p

### 2.2.0
- you no longer have to click the window with mouse first to be able to use keyboard inputs
- new audio delay calibration thing to see how delayed ur audio thing is it also will account for input lag i guess

### 2.1.1
- fixed bug now Lockstep 2 also fades to black at the end

### 2.1.0
- fade in and fade out of game
- you can't accidentally misinput after the end anymore
- closing the frame stops the program
- intro title card added

### 2.0.1
- you now don't get hit twice in a row if you miss again

### 2.0.0
- added Lockstep 2
- added Main arguments 
 - so you can now mix and match parts from Lockstep and Lockstep two

### 1.0.0
- made the thing