package canaryprism.unlockstep;

import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.JFrame;

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
        for (int i = args.length - 1; i >= 0; i--) {
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

        enum GameMode {
            lockstep1, lockstep2
        }

        var game = getArg(args, "--game", (e) -> {
            return switch (e) {
                case "lockstep1" -> GameMode.lockstep1;
                case "lockstep2" -> GameMode.lockstep2;
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
                default -> throw new IllegalArgumentException("Invalid music: " + e);
            }, 
            () -> switch (game) {
                case lockstep1 -> "/unlockstep_assets/music/lockstep1.wav";
                case lockstep2 -> "/unlockstep_assets/music/lockstep2.wav";
            });

        var sprite_path = getArg(args, "--sprite", 
            (e) -> switch (e) {
                case "lockstep1" -> "/unlockstep_assets/sprites/lockstep1";
                case "lockstep2" -> "/unlockstep_assets/sprites/lockstep2";
                
                default -> throw new IllegalArgumentException("Invalid sprites: " + e);
            }, 
            () -> switch (game) {
                case lockstep1 -> "/unlockstep_assets/sprites/lockstep1";
                case lockstep2 -> "/unlockstep_assets/sprites/lockstep2";
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
                
                default -> throw new IllegalArgumentException("Invalid color palette: " + e);
            }, 
            () -> switch (sprite_path) {
                case "/unlockstep_assets/sprites/lockstep1" -> ColorPalette.lockstep1;
                case "/unlockstep_assets/sprites/lockstep2" -> ColorPalette.lockstep2;
                default -> throw new RuntimeException("Invalid sprite path somehow: " + sprite_path);
            });

        var lockstep = switch (game) {
            case lockstep1 -> 
                new Lockstep(new JFrame("Unlockstep"), music_path, "/unlockstep_assets/audio", sprite_path, color_palette);
            case lockstep2 -> 
                new Lockstep2(new JFrame("Unlockstep"), music_path, "/unlockstep_assets/audio", sprite_path, color_palette);
        };

        lockstep.start().join();
        
    }
}
