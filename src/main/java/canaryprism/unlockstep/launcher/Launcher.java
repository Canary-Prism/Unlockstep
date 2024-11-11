package canaryprism.unlockstep.launcher;

import canaryprism.unlockstep.*;
import canaryprism.unlockstep.calibration.LagCalibration;
import canaryprism.unlockstep.intro.IntroTitleCard;
import canaryprism.unlockstep.swing.ColorPalette;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;

public class Launcher extends JComponent {

    public Launcher(JFrame frame) {
        var layout = new SpringLayout();
        this.setLayout(layout);

        // game mode selection

        var gamemode_label = new JLabel("Game: ", JLabel.TRAILING);
        var gamemode_combobox = new JComboBox<>(GameMode.values());

        gamemode_combobox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return super.getListCellRendererComponent(list, ((GameMode) value).name, index, isSelected, cellHasFocus);
            }
        });

        this.add(gamemode_label);
        this.add(gamemode_combobox);
        
        var music_label = new JLabel("Music: ", JLabel.TRAILING);
        var music_combobox = new JComboBox<>(Main.music_paths.sequencedKeySet().toArray());

        music_combobox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return super.getListCellRendererComponent(list, ((GameMode) value).name, index, isSelected, cellHasFocus);
            }
        });

        this.add(music_label);
        this.add(music_combobox);

        var sprite_label = new JLabel("Sprite: ", JLabel.TRAILING);
        var sprite_combobox = new JComboBox<>(Main.sprite_paths.sequencedKeySet().toArray());

        sprite_combobox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return super.getListCellRendererComponent(list, ((GameMode) value).name, index, isSelected, cellHasFocus);
            }
        });

        this.add(sprite_label);
        this.add(sprite_combobox);


        var palette_label = new JLabel("Colour Palette: ", JLabel.TRAILING);
        var palette_combobox = new JComboBox<>(Main.color_palettes.sequencedKeySet().toArray());

        palette_combobox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return super.getListCellRendererComponent(list, ((GameMode) value).name, index, isSelected, cellHasFocus);
            }
        });

        this.add(palette_label);
        this.add(palette_combobox);

        var intro_label = new JLabel("Intro Card: ", JLabel.TRAILING);
        var intro_combobox = new JComboBox<>(Main.intro_paths.sequencedKeySet().toArray());

        intro_combobox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return super.getListCellRendererComponent(list, ((GameMode) value).name, index, isSelected, cellHasFocus);
            }
        });

        this.add(intro_label);
        this.add(intro_combobox);

        var mode_label = new JLabel("Mode: ", JLabel.TRAILING);
        var mode_buttons = new ButtonGroup();

        var button_panel = new JPanel();
        button_panel.setLayout(new BoxLayout(button_panel, BoxLayout.Y_AXIS));

        var normal_radio = new JRadioButton("Normal");
        mode_buttons.add(normal_radio);
        button_panel.add(normal_radio);

        var perfect_radio = new JRadioButton("Perfect Campaign");
        mode_buttons.add(perfect_radio);
        button_panel.add(perfect_radio);

        var auto_radio = new JRadioButton("Auto");
        mode_buttons.add(auto_radio);
        button_panel.add(auto_radio);

        normal_radio.setSelected(true);

        this.add(mode_label);
        this.add(button_panel);



        var delay_checkbox = new JLabel("Manual Audio Latency (milliseconds): ", JLabel.TRAILING);

        var delay_panel = new JPanel();

        var delay_calibrate_button = new JButton("Calibrate");
        var delay_spinner = new JSpinner(new SpinnerNumberModel(0, 0, 2147483647, 10));

        delay_calibrate_button.addActionListener((_) -> {

            var calibration_frame = new JFrame();

            calibration_frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

            var calibration = new LagCalibration(calibration_frame, "/unlockstep_assets/audio",
                    "/unlockstep_assets/music/practise.wav");
            calibration_frame.pack();
            calibration_frame.setVisible(true);

            calibration.start().thenAccept((audio_delay) -> {
                delay_spinner.setValue(audio_delay);
                System.out.println("Audio delay: " + audio_delay);
            }).handle((_, _) -> {
                calibration_frame.dispose();
                delay_spinner.requestFocus();
                return null;
            });

        });

        delay_panel.add(delay_calibrate_button);
        delay_panel.add(delay_spinner);

        this.add(delay_checkbox);
        this.add(delay_panel);

        gamemode_combobox.addItemListener((e) -> {
            music_combobox.setSelectedItem(e.getItem());
            sprite_combobox.setSelectedItem(e.getItem());
            intro_combobox.setSelectedItem(e.getItem());
        });

        sprite_combobox.addItemListener((e) -> palette_combobox.setSelectedItem(e.getItem()));

        var start_button = new JButton("Start");

        this.add(Box.createGlue());
        this.add(start_button);
        
        start_button.addActionListener((_) -> {
            frame.getContentPane().removeAll();
            Thread.ofVirtual().start(() -> startGame(
                    frame,
                    ((GameMode) gamemode_combobox.getSelectedItem()),
                    Main.music_paths.get(((GameMode) music_combobox.getSelectedItem())),
                    Main.sprite_paths.get(((GameMode) sprite_combobox.getSelectedItem())),
                    Main.color_palettes.get(((GameMode) palette_combobox.getSelectedItem())),
                    false,
                    auto_radio.isSelected(),
                    perfect_radio.isSelected(),
                    ((Number) delay_spinner.getValue()).longValue(),
                    Main.intro_paths.get(((GameMode) intro_combobox.getSelectedItem())),
                    Main.intro_volume.get(((GameMode) intro_combobox.getSelectedItem()))
            ));
        });

        makeCompactGrid(this, 8, 2, 5, 5, 5, 5);

    }


    private static void startGame(JFrame frame, GameMode game, String music_path, String sprite_path, ColorPalette color_palette, boolean player_input_sound, boolean auto, boolean perfect, long audio_delay, String intro_path, float intro_volume) {

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
            case remix1 ->
                    new LockstepR1(frame, music_path, "/unlockstep_assets/audio", sprite_path, color_palette, player_input_sound);
            case endless_remix ->
                    new LockstepER(frame, music_path, "/unlockstep_assets/audio", sprite_path, color_palette, player_input_sound);
            case wip1 -> {
                JOptionPane.showMessageDialog(null, "wip1 not allowed");
                throw new IllegalArgumentException("");
            }
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

    // stolen from oracle hehe

    /* Used by makeCompactGrid. */
    private static SpringLayout.Constraints getConstraintsForCell(
            int row, int col,
            Container parent,
            int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

    /**
     * Aligns the first <code>rows</code> * <code>cols</code>
     * components of <code>parent</code> in
     * a grid. Each component in a column is as wide as the maximum
     * preferred width of the components in that column;
     * height is similarly determined for each row.
     * The parent is made just big enough to fit them all.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad x padding between cells
     * @param yPad y padding between cells
     */
    public static void makeCompactGrid(Container parent,
                                       int rows, int cols,
                                       int initialX, int initialY,
                                       int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

        //Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                        getConstraintsForCell(r, c, parent, cols).
                                getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        //Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                        getConstraintsForCell(r, c, parent, cols).
                                getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }

    public static void main() {
        FlatMacDarkLaf.setup();

        var frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        frame.getContentPane().add(new Launcher(frame));

        frame.pack();
        frame.setVisible(true);
    }
}
