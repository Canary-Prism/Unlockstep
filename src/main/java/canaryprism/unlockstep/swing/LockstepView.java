package canaryprism.unlockstep.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import canaryprism.unlockstep.Conductor;
import canaryprism.unlockstep.Lockstep;

public class LockstepView extends JComponent {

    private final Image bach_formation_left, bach_formation_right;
        
    public LockstepView(int fps, Lockstep lockstep, String sprite_path) {

        this.sprite_sets = new HashMap<>();
        sprite_sets.put(ZoomSize.l0, new StepswitcherSpriteSet(ZoomSize.l0, sprite_path + "/stepswitcher-0"));
        sprite_sets.put(ZoomSize.l1, new StepswitcherSpriteSet(ZoomSize.l1, sprite_path + "/stepswitcher-1"));
        sprite_sets.put(ZoomSize.l2, new StepswitcherSpriteSet(ZoomSize.l2, sprite_path + "/stepswitcher-2"));
        sprite_sets.put(ZoomSize.l3, new StepswitcherSpriteSet(ZoomSize.l3, sprite_path + "/stepswitcher-3"));
        sprite_sets.put(ZoomSize.l4, new StepswitcherSpriteSet(ZoomSize.l4, sprite_path + "/stepswitcher-4"));

        try {
            this.bach_formation_left = ImageIO.read(Lockstep.getResource(sprite_path + "/bach_formation_left.png"));
            this.bach_formation_right = ImageIO.read(Lockstep.getResource(sprite_path + "/bach_formation_right.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                requestFocusInWindow();
                
                lockstep.playerTap();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
            }
        });

        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    lockstep.playerTap();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
            
        });

        var conductor = new Conductor(1000, fps);
        conductor.submit((r) -> {
            advanceFrame();
            repaint();
        });
        conductor.start(0);
    }

    private final Object viewlock = new Object();

    private final HashMap<ZoomSize, StepswitcherSpriteSet> sprite_sets;

    private volatile StepswitcherPose crowd_pose = StepswitcherPose.idle;
    private volatile ZoomSize size = ZoomSize.l0;

    private volatile ZoomSize next_size = null;

    private volatile List<StepswitcherPose> crowd_animation;
    private volatile int crowd_animation_frame;

    private volatile StepswitcherPose player_pose = StepswitcherPose.idle;
    private volatile List<StepswitcherPose> player_animation;
    private volatile int player_animation_frame;

    private volatile Color background_color;

    public void crowdAnimate(Animation animation) {
        synchronized (viewlock) {
            crowd_animation = animation.frames;
            crowd_animation_frame = 0;
        }
    }

    public void playerAnimate(Animation animation) {
        synchronized (viewlock) {
            player_animation = animation.frames;
            player_animation_frame = 0;
        }
    }

    public void allAnimate(Animation animation) {
        synchronized (viewlock) {
            this.crowd_animation = animation.frames;
            this.crowd_animation_frame = 0;
            this.player_animation = animation.frames;
            this.player_animation_frame = 0;
        }
    }

    public void zoom(ZoomSize size) {
        synchronized (viewlock) {
            this.next_size = size;
        }
    }

    public void setBackgroundColor(Color color) {
        synchronized (viewlock) {
            this.background_color = color;
        }
    }

    private void advanceFrame() {
        synchronized (viewlock) {
            if (next_size != null) {
                size = next_size;
                next_size = null;
            }
            if (crowd_animation != null) {
                crowd_pose = crowd_animation.get(crowd_animation_frame);
                crowd_animation_frame++;
                if (crowd_animation_frame >= crowd_animation.size()) {
                    crowd_animation = null;
                }
            }
            if (player_animation != null) {
                player_pose = player_animation.get(player_animation_frame);
                player_animation_frame++;
                if (player_animation_frame >= player_animation.size()) {
                    player_animation = null;
                }
            }
        }
    }

    @Override
    protected void paintComponent(java.awt.Graphics g1) {
        synchronized (viewlock) {
            var g = (java.awt.Graphics2D)g1;
    
            var scale = Math.max(getWidth() / 200d, getHeight() / 256d);
    
            g.setColor(background_color);
    
            g.fillRect(0, 0, getWidth(), getHeight());
    
            double screenx = getWidth() / 2 - (200 * scale / 2);
            double screeny = getHeight() / 2 - (256 * scale / 2);
            boolean offset = size.startsOffset();
    
            var crowd_sprite = sprite_sets.get(size).getSprite(crowd_pose);
            var player_sprite = sprite_sets.get(size).getSprite(player_pose);
            
            for (int y = size.startY(), yk = 0; yk < size.countY(); y += size.distY(), yk++) {
                for (int x = size.startX() + (offset ? size.offsetX() : 0), xk = 0; xk < size.countX(); x += size.distX(), xk++) {
                    if (xk == size.playerX() && yk == size.playerY())
                        drawSprite(player_pose, player_sprite, g, x * scale + screenx, y * scale + screeny, scale);
                    else
                        drawSprite(crowd_pose, crowd_sprite, g, x * scale + screenx, y * scale + screeny, scale);
                }
                offset = !offset;
            }

            if (size == ZoomSize.l4 && (crowd_pose == StepswitcherPose.left_extend || crowd_pose == StepswitcherPose.left_pose)) {
                // g.fillRect(0, 0, getWidth(), getHeight());

                g.drawImage(bach_formation_left, (int)Math.round(screenx + 0 * scale), (int)Math.round(screeny * scale), (int)Math.round(bach_formation_left.getWidth(null) * scale), (int)Math.round(bach_formation_left.getHeight(null) * scale), null);
            } else if (size == ZoomSize.l4 && (crowd_pose == StepswitcherPose.right_extend || crowd_pose == StepswitcherPose.right_pose)) {
                g.drawImage(bach_formation_right, (int)Math.round(screenx + 0 * scale), (int)Math.round(screeny * scale), (int)Math.round(bach_formation_right.getWidth(null) * scale), (int)Math.round(bach_formation_right.getHeight(null) * scale), null);
            }
        }
    }

    private void drawSprite(StepswitcherPose pose, Image sprite, Graphics2D g, double x, double y, double scale) {
        x -= pose.spriteOffset(size) * scale;
        y -= sprite.getHeight(null) * scale;
        g.drawImage(sprite, (int)Math.round(x), (int)Math.round(y), (int)Math.round(sprite.getWidth(null) * scale), (int)Math.round(sprite.getHeight(null) * scale), null);
    }
}
