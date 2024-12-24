import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int BIRD_SIZE = 20;
    private final int PIPE_WIDTH = 100;
    private final int PIPE_GAP = 150;
    private final int GRAVITY = 1;
    private final int JUMP_STRENGTH = 15;

    private int birdY = HEIGHT / 2;
    private int birdVelocity = 0;
    private ArrayList<Rectangle> pipes;
    private int score = 0;
    private boolean gameOver = false;

    public FlappyBird() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.cyan);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
                    birdVelocity = -JUMP_STRENGTH;
                }
            }
        });

        pipes = new ArrayList<>();
        Timer timer = new Timer(20, this);
        timer.start();
        spawnPipe();
    }

    private void spawnPipe() {
        int pipeHeight = new Random().nextInt(HEIGHT - PIPE_GAP - 100) + 50;
        pipes.add(new Rectangle(WIDTH, 0, PIPE_WIDTH, pipeHeight));
        pipes.add(new Rectangle(WIDTH, pipeHeight + PIPE_GAP, PIPE_WIDTH, HEIGHT - pipeHeight - PIPE_GAP));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            birdVelocity += GRAVITY;
            birdY += birdVelocity;

            if (birdY > HEIGHT || birdY < 0) {
                gameOver = true;
            }

            for (int i = 0; i < pipes.size(); i++) {
                Rectangle pipe = pipes.get(i);
                pipe.x -= 5;

                if (pipe.x + PIPE_WIDTH < 0) {
                    pipes.remove(i);
                    i--;
                    score++;
                }

                if (pipe.intersects(new Rectangle(WIDTH / 2 - BIRD_SIZE / 2, birdY, BIRD_SIZE, BIRD_SIZE))) {
                    gameOver = true;
                }
            }

            if (pipes.isEmpty() || pipes.get(pipes.size() - 1).x < WIDTH - 300) {
                spawnPipe();
            }

            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.YELLOW);
        g.fillRect(WIDTH / 2 - BIRD_SIZE / 2, birdY, BIRD_SIZE, BIRD_SIZE);

        g.setColor(Color.GREEN);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);

        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over!", WIDTH / 2 - 30, HEIGHT / 2);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}