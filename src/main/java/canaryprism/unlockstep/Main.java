package canaryprism.unlockstep;

import java.util.function.Function;
import java.util.function.Supplier;
import javax.swing.JFrame;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import canaryprism.unlockstep.calibration.LagCalibration;
import canaryprism.unlockstep.intro.IntroTitleCard;
import canaryprism.unlockstep.swing.ColorPalette;

public class Main {

    static boolean hasArg(String[] args, String key) {
        for (int i = args.length - 1; i >= 0; i--) {
            if (args[i].equals(key)) {
                return true;
            }
        }
        return false;
    }
    static String getArg(String[] args, String key, Supplier<String> default_value) {
        return getArg(args, key, Function.identity(), default_value);
    }
    static String getArg(String[] args, String key) {
        return getArg(args, key, () -> { throw new IllegalArgumentException("No value for key " + key); });
    }
    static <T> T getArg(String[] args, String key, Function<String, T> parser, Supplier<T> default_value) {
        for (int i = args.length - 2; i >= 0; i--) {
            if (args[i].equals(key)) {
                return parser.apply(args[i + 1]);
            }
        }
        return default_value.get();
    }

    static void warnMusicMismatch(String music, String game) {
        System.out.println(STR."""
                Warning: music \{music} does not match game mode \{game}
                         the rhythm will be off
                """);
    }

    static void warnColorMismatch(String palette, String sprite) {
        System.out.println(STR."""
                Warning: color palette \{palette} does not match sprite set \{sprite}
                         it'll look ugly
                """);
    }

    static volatile int bpm = 120;

    public static void main(String[] args) {

        // var cowbell = new AudioPlayer(Lockstep.getResource("/unlockstep_assets/audio/sfx/cowbell.wav"), 3);
        // var music = new AudioPlayer(Lockstep.getResource("/unlockstep_assets/music/remix8.wav"), 1);

        
        // try {

        //     var timer = new Timer();

        //     var conductor = new Conductor(60_000_000, 171184);

        //     final var first_change = 18 - 8;

        //     conductor.submit((e) -> {
        //         if (e.beat() == 143 - 32) {
        //             System.out.println("Changing tempo");
        //             conductor.setTempo(60_000, 172);
        //         } else if (e.beat() == 143 + 8) {
        //             System.out.println("Changing tempo");
        //             conductor.setTempo(60_000_000, 171184);
        //         } else if (e.beat() == first_change) {
        //             System.out.println("Changing tempo");
        //             conductor.setTempo(60_000, 182);
        //         } else if (e.beat() == first_change + 4 * 6) {
        //             System.out.println("Changing tempo");
        //             conductor.setTempo(60_000, 185);
        //         } else if (e.beat() == first_change + 4 * 6 + 4 * 2) {
        //             System.out.println("Changing tempo");
        //             conductor.setTempo(60_000, 190);
        //         } else if (e.beat() == first_change + 4 * 6 + 4 * 2 + 4 * 2) {
        //             System.out.println("Changing tempo");
        //             conductor.setTempo(60_000, 198);
        //         } else if (e.beat() == first_change + 4 * 6 + 4 * 2 + 4 * 2 + 4 * 2) {
        //             System.out.println("Changing tempo");
        //             conductor.setTempo(60_000, 213);
        //         } else if (e.beat() == first_change + 4 * 6 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2) {
        //             System.out.println("Changing tempo");
        //             conductor.setTempo(60_000, 216);
        //         } else if (e.beat() == first_change + 4 * 6 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2) {
        //             System.out.println("Changing tempo");
        //             conductor.setTempo(60_000, 218);
        //         } else if (e.beat() == first_change + 4 * 6 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2 + 4 * 2 + 4) {
        //             System.out.println("Changing tempo");
        //             conductor.setTempo(60_000, 220);
        //         }
        //     });

        //     conductor.submit((e) -> {
        //         cowbell.play();
        //         System.out.println("cowbell " + e.beat());
        //     });

        //     conductor.start(0);


        //     timer.schedule(new TimerTask() {
        //         @Override
        //         public void run() {
        //             // music.play();
        //         }
        //     }, 60_000 / 171 - 80);


        //     Thread.currentThread().join();
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        FlatMacDarkLaf.setup();

        enum GameMode {
            lockstep1, lockstep2, remix6, remix8, remix9
        }

        var game = getArg(args, "--game", (e) -> {
            return switch (e) {
                case "lockstep1" -> GameMode.lockstep1;
                case "lockstep2" -> GameMode.lockstep2;
                case "remix6" -> GameMode.remix6;
                case "remix8" -> GameMode.remix8;
                case "remix9" -> GameMode.remix9;
                default -> throw new IllegalArgumentException("Invalid game mode: " + e);
            };
        }, () -> GameMode.lockstep1);

        var music_path = getArg(args, "--music", 
            (e) -> switch (e) {
                case "lockstep1" -> {
                    if (game != GameMode.lockstep1) 
                        warnMusicMismatch(e, game.name());
                    
                    yield "/unlockstep_assets/music/lockstep1.wav";
                }
                case "lockstep2" -> {
                    if (game != GameMode.lockstep2) 
                        warnMusicMismatch(e, game.name());
                    
                    yield "/unlockstep_assets/music/lockstep2.wav";
                }
                case "remix6" -> {
                    if (game != GameMode.remix6) 
                        warnMusicMismatch(e, game.name());
                    
                    yield "/unlockstep_assets/music/remix6.wav";
                }
                case "remix8" -> {
                    if (game != GameMode.remix8) 
                        warnMusicMismatch(e, game.name());
                    
                    yield "/unlockstep_assets/music/remix8.wav";
                }
                case "remix9" -> {
                    if (game != GameMode.remix9) 
                        warnMusicMismatch(e, game.name());
                    
                    yield "/unlockstep_assets/music/remix9.wav";
                }
                default -> throw new IllegalArgumentException("Invalid music: " + e);
            }, 
            () -> switch (game) {
                case lockstep1 -> "/unlockstep_assets/music/lockstep1.wav";
                case lockstep2 -> "/unlockstep_assets/music/lockstep2.wav";
                case remix6 -> "/unlockstep_assets/music/remix6.wav";
                case remix8 -> "/unlockstep_assets/music/remix8.wav";
                case remix9 -> "/unlockstep_assets/music/remix9.wav";
            });

        var sprite_path = getArg(args, "--sprite", 
            (e) -> switch (e) {
                case "lockstep1" -> "/unlockstep_assets/sprites/lockstep1";
                case "lockstep2" -> "/unlockstep_assets/sprites/lockstep2";
                case "remix6" -> "/unlockstep_assets/sprites/remix6";
                case "remix8" -> "/unlockstep_assets/sprites/remix8";
                case "remix9" -> "/unlockstep_assets/sprites/remix9";
                
                default -> throw new IllegalArgumentException("Invalid sprites: " + e);
            }, 
            () -> switch (game) {
                case lockstep1 -> "/unlockstep_assets/sprites/lockstep1";
                case lockstep2 -> "/unlockstep_assets/sprites/lockstep2";
                case remix6 -> "/unlockstep_assets/sprites/remix6";
                case remix8 -> "/unlockstep_assets/sprites/remix8";
                case remix9 -> "/unlockstep_assets/sprites/remix9"; 
            });

        var color_palette = getArg(args, "--color", 
            (e) -> switch (e) {
                case "lockstep1" -> {
                    if (game != GameMode.lockstep1) 
                        warnColorMismatch(e, sprite_path);
                    
                    yield ColorPalette.lockstep1;
                }
                case "lockstep2" -> {
                    if (game != GameMode.lockstep2) 
                        warnColorMismatch(e, sprite_path);
                    
                    yield ColorPalette.lockstep2;
                }
                case "remix6" -> {
                    if (game != GameMode.remix6) 
                        warnColorMismatch(e, sprite_path);
                    
                    yield ColorPalette.remix6;
                }
                case "remix8" -> {
                    if (game != GameMode.remix8) 
                        warnColorMismatch(e, sprite_path);
                    
                    yield ColorPalette.remix8;
                }
                case "remix9" -> {
                    if (game != GameMode.remix9) 
                        warnColorMismatch(e, sprite_path);
                    
                    yield ColorPalette.remix9;
                }

                
                default -> throw new IllegalArgumentException("Invalid color palette: " + e);
            }, 
            () -> switch (sprite_path) {
                case "/unlockstep_assets/sprites/lockstep1" -> ColorPalette.lockstep1;
                case "/unlockstep_assets/sprites/lockstep2" -> ColorPalette.lockstep2;
                case "/unlockstep_assets/sprites/remix6" -> ColorPalette.remix6;
                case "/unlockstep_assets/sprites/remix8" -> ColorPalette.remix8;
                case "/unlockstep_assets/sprites/remix9" -> ColorPalette.remix9;
                default -> throw new RuntimeException("Invalid sprite path somehow: " + sprite_path);
            });


        var intro_path = getArg(args, "--intro", 
            (e) -> switch (e) {
                case "lockstep1" -> "/unlockstep_assets/intro/lockstep1";
                case "lockstep2" -> "/unlockstep_assets/intro/lockstep2";
                case "remix6" -> "/unlockstep_assets/intro/remix6";
                case "remix8" -> "/unlockstep_assets/intro/remix8";
                case "remix9" -> "/unlockstep_assets/intro/remix9";
                case "skip" -> null;
                default -> throw new IllegalArgumentException("Invalid intro: " + e);
            }, 
            () -> switch (game) {
                case lockstep1 -> "/unlockstep_assets/intro/lockstep1";
                case lockstep2 -> "/unlockstep_assets/intro/lockstep2";
                case remix6 -> "/unlockstep_assets/intro/remix6";
                case remix8 -> "/unlockstep_assets/intro/remix8";
                case remix9 -> "/unlockstep_assets/intro/remix9";
            });

        float intro_volume = getArg(args, "--intro", 
            (e) -> switch (e) {
                case "lockstep1" -> 2;
                case "lockstep2" -> 2;
                case "remix6" -> 1;
                case "remix8" -> 1;
                case "remix9" -> 1;
                case "skip" -> 1;
                default -> throw new IllegalArgumentException("Invalid intro: " + e);
            }, 
            () -> switch (game) {
                case lockstep1 -> 2;
                case lockstep2 -> 2;
                case remix6 -> 1;
                case remix8 -> 1;
                case remix9 -> 1;
            });

        var audio_delay = getArg(args, "--audio-delay", Long::parseLong, () -> null);
        var frame = new JFrame("Unlockstep");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if (audio_delay == null) {
            var calibration = new LagCalibration(frame, "/unlockstep_assets/audio",
                    "/unlockstep_assets/music/practise.wav");
            frame.pack();
            frame.setVisible(true);

            audio_delay = calibration.start().join();
            System.out.println("Audio delay: " + audio_delay);
            frame.getContentPane().removeAll();
        }

        // final long audio_delay_final = audio_delay;

        boolean player_input_sound = getArg(args, "--player-input-sound", (e) -> {
            return switch (e) {
                case "true" -> true;
                case "false" -> false;
                default -> throw new IllegalArgumentException("Invalid player input sound: " + e);
            };
        }, () -> false);

        boolean auto = hasArg(args, "--auto");
        boolean perfect = hasArg(args, "--perfect");

        if (auto && perfect) {
            throw new IllegalArgumentException("Cannot have both auto and perfect mode");
        }


        frame.setSize(200 * 3, 256 * 3);
        if (intro_path != null) {
            var intro = new IntroTitleCard(intro_path, frame, intro_volume);
    
            frame.setVisible(true);
    
            intro.start().join();
    
            frame.getContentPane().removeAll();
        }

        var lockstep = switch (game) {
            case lockstep1 -> 
                new Lockstep(frame, music_path, "/unlockstep_assets/audio", sprite_path, color_palette, player_input_sound);
            case lockstep2 -> 
                new Lockstep2(frame, music_path, "/unlockstep_assets/audio", sprite_path, color_palette, player_input_sound);
            case remix6 ->
                new LockstepR6(frame, music_path, "/unlockstep_assets/audio", sprite_path, color_palette, player_input_sound);
            case remix8 ->
                new LockstepR8(frame, music_path, "/unlockstep_assets/audio", sprite_path, color_palette, player_input_sound);
            case remix9 ->
                new LockstepR9(frame, music_path, "/unlockstep_assets/audio", sprite_path, color_palette, player_input_sound);
        };

        lockstep.setAuto(auto);
        if (perfect) {
            lockstep.loadIndicator("/unlockstep_assets/indicators/perfect");
        } else if (auto) {
            lockstep.loadIndicator("/unlockstep_assets/indicators/auto");
        }

        lockstep.setAudioDelay(audio_delay);

        lockstep.start().join();
        
    }
}
