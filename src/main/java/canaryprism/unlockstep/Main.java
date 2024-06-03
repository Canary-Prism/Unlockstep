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


    public static void main(String[] args) {

        FlatMacDarkLaf.setup();

        enum GameMode {
            lockstep1, lockstep2, remix6
        }

        var game = getArg(args, "--game", (e) -> {
            return switch (e) {
                case "lockstep1" -> GameMode.lockstep1;
                case "lockstep2" -> GameMode.lockstep2;
                case "remix6" -> GameMode.remix6;
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
                default -> throw new IllegalArgumentException("Invalid music: " + e);
            }, 
            () -> switch (game) {
                case lockstep1 -> "/unlockstep_assets/music/lockstep1.wav";
                case lockstep2 -> "/unlockstep_assets/music/lockstep2.wav";
                case remix6 -> "/unlockstep_assets/music/remix6.wav";
            });

        var sprite_path = getArg(args, "--sprite", 
            (e) -> switch (e) {
                case "lockstep1" -> "/unlockstep_assets/sprites/lockstep1";
                case "lockstep2" -> "/unlockstep_assets/sprites/lockstep2";
                case "remix6" -> "/unlockstep_assets/sprites/remix6";
                
                default -> throw new IllegalArgumentException("Invalid sprites: " + e);
            }, 
            () -> switch (game) {
                case lockstep1 -> "/unlockstep_assets/sprites/lockstep1";
                case lockstep2 -> "/unlockstep_assets/sprites/lockstep2";
                case remix6 -> "/unlockstep_assets/sprites/remix6";
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
                
                default -> throw new IllegalArgumentException("Invalid color palette: " + e);
            }, 
            () -> switch (sprite_path) {
                case "/unlockstep_assets/sprites/lockstep1" -> ColorPalette.lockstep1;
                case "/unlockstep_assets/sprites/lockstep2" -> ColorPalette.lockstep2;
                case "/unlockstep_assets/sprites/remix6" -> ColorPalette.remix6;
                default -> throw new RuntimeException("Invalid sprite path somehow: " + sprite_path);
            });


        var intro_path = getArg(args, "--intro", 
            (e) -> switch (e) {
                case "lockstep1" -> "/unlockstep_assets/intro/lockstep1";
                case "lockstep2" -> "/unlockstep_assets/intro/lockstep2";
                case "remix6" -> "/unlockstep_assets/intro/remix6";
                case "skip" -> null;
                default -> throw new IllegalArgumentException("Invalid intro: " + e);
            }, 
            () -> switch (game) {
                case lockstep1 -> "/unlockstep_assets/intro/lockstep1";
                case lockstep2 -> "/unlockstep_assets/intro/lockstep2";
                case remix6 -> "/unlockstep_assets/intro/remix6";
            });

        float intro_volume = getArg(args, "--intro", 
            (e) -> switch (e) {
                case "lockstep1" -> 2;
                case "lockstep2" -> 2;
                case "remix6" -> 1;
                case "skip" -> 1;
                default -> throw new IllegalArgumentException("Invalid intro: " + e);
            }, 
            () -> switch (game) {
                case lockstep1 -> 2;
                case lockstep2 -> 2;
                case remix6 -> 1;
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

        final long audio_delay_final = audio_delay;

        boolean player_input_sound = getArg(args, "--player-input-sound", (e) -> {
            return switch (e) {
                case "true" -> true;
                case "false" -> false;
                default -> throw new IllegalArgumentException("Invalid player input sound: " + e);
            };
        }, () -> audio_delay_final < 30);


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
        };

        lockstep.setAudioDelay(audio_delay);

        lockstep.start().join();
        
    }
}
