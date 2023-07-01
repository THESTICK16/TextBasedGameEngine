import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.LinkedList;

public class Map extends JPanel {

    private char[][] map;

    private final Dimension MAP_SCREEN_SIZE = new Dimension(1200, 575);

    private final int VERTICAL_SPACING = 20;

    private final int NUM_VISIBLE_CHARS_PER_ROW = 60; //60

    private final int NUM_VISIBLE_ROWS = (int) (MAP_SCREEN_SIZE.getHeight() / VERTICAL_SPACING); //30

    private final char PLAYER = 'P';

    private final char GOAL = 'G';

    private final String LOOK = "";

    private int mapHeight;

    private int mapWidth;

    private ArrayList<JLabel> rows;

    private Dimension goalCoordinates;

    public GameWindow gameWindow;

    Dimension playerLocation;
    Dimension lookLocation;


    public Map() {
        this.mapHeight = 60;
        this.mapWidth = 120;
        this.rows = new ArrayList<>();
        map = new char[this.mapWidth][this.mapHeight];
        playerLocation = new Dimension((int) mapWidth / 2, (int) mapHeight / 2);
        lookLocation = new Dimension((int) mapWidth + 1 / 2, (int) mapHeight + 1 / 2);
        goalCoordinates = new Dimension(-1, -1);
    }

    public Map(Dimension playerLocation) {
        this.mapHeight = 60;
        this.mapWidth = 150;
        this.rows = new ArrayList<>();
        map = new char[this.mapWidth][this.mapHeight];
        this.playerLocation = playerLocation; //null catch in GameWindow class in mapWindow method
        lookLocation = new Dimension(0, 0);
        lookLocation.height = playerLocation.height + 1;
        lookLocation.width = playerLocation.width + 1;
        goalCoordinates = new Dimension(-1, -1);
    }

    public void addToPanel(JPanel panel) {
        for(int i = 0; i < mapHeight; i++) {
            JLabel newLabel = new JLabel();
            newLabel.setForeground(Color.WHITE);
            //newLabel.setFont(new Font("American Typewriter", Font.PLAIN, 15));
            newLabel.setSize((int) MAP_SCREEN_SIZE.getWidth(), VERTICAL_SPACING);
            newLabel.setBounds(300, i * VERTICAL_SPACING, (int) MAP_SCREEN_SIZE.getWidth(), VERTICAL_SPACING);
            rows.add(newLabel);
            panel.add(newLabel);
        }
        panel.addKeyListener(new ArrowKeyListener());
    }


    public void loadMap() {
        Random charRand = new Random();
        for(int i = 0; i < mapHeight; i++) {
            for(int j = 0; j < mapWidth; j++) {
                char nextChar;
                if(i % 2 == 0 && j % 2 == 0 && charRand.nextInt(10) < 3) {
                    nextChar = '/';
                } else {
                    nextChar = '#';
                }

                map[j][i] = nextChar;
            }
        }
    }

    public void drawMap() {
        int mapTop = (int) (lookLocation.getHeight() - NUM_VISIBLE_ROWS / 2);
        int mapLeft = (int) (lookLocation.getWidth() - NUM_VISIBLE_CHARS_PER_ROW / 2);
        char[][] visibleMap = new char[NUM_VISIBLE_CHARS_PER_ROW][NUM_VISIBLE_ROWS];

        for(int i = 0; i < visibleMap[0].length; i++) {
            StringBuilder nextRow = new StringBuilder();

            for(int j = 0; j < visibleMap.length; j++) {
                if(i + mapTop < 0 || j + mapLeft < 0 || i + mapTop > map[0].length - 1 || j + mapLeft > map.length - 1) {
                    nextRow.append(' ' + " ");
                } else if(j + mapLeft == lookLocation.getWidth() && i + mapTop == lookLocation.getHeight()) {
                    nextRow.append(LOOK);
                } else if (j + mapLeft == playerLocation.getWidth() && i + mapTop == playerLocation.getHeight()) {
                    nextRow.append(PLAYER + " ");
                }
                else if(goalCoordinates != null && j + mapLeft == (int) goalCoordinates.getWidth() && i + mapTop == (int) goalCoordinates.getHeight()) {
                    nextRow.append(GOAL + " ");
                }
                else {
                    nextRow.append(map[j + mapLeft][i + mapTop] + " ");
                }
            }
            rows.get(i).setText(nextRow.toString());
        }

//        for (int i = 0; i < visibleMap[0].length; i++) {
//            StringBuilder nextRow  = new StringBuilder();
//
//            for (int j = 0; j < visibleMap.length; j++) {
//                nextRow.append(visibleMap[j][i] + " ");
//            }
//            rows.get(i).setText(nextRow.toString());
//        }
    }

    private void moveVertically(boolean up) {
        if (up && playerLocation.getHeight() > 0) {
//            topRowIndex -= 1;
            playerLocation.height -= 1;
        } else if (!up && playerLocation.getHeight() < mapHeight - 1) {
//            topRowIndex += 1;
            playerLocation.height += 1;
        }
    }

        private void lookVertically ( boolean up){
            if (up && lookLocation.getHeight() > 0) {
                lookLocation.height -= 1;
            } else if (!up && lookLocation.getHeight() < mapHeight - 1) {
                lookLocation.height += 1;
            }
            drawMap();
        }

        private void moveHorizontally ( boolean left){
            if (left && playerLocation.getWidth() > 0) {
//            leftColumnIndex -= 1;
                playerLocation.width -= 1;
            } else if (!left && playerLocation.getWidth() < mapWidth - 1) {
//            leftColumnIndex += 1;
                playerLocation.width += 1;
            }
            drawMap();
        }

        private void lookHorizontally ( boolean left){
            if (left && lookLocation.getWidth() > 0) {
                lookLocation.width -= 1;
            } else if (!left && lookLocation.getWidth() < mapWidth - 1) {
                lookLocation.width += 1;
            }
            drawMap();
        }

        public void setPlayerLocation (Dimension playerLocation){
            this.playerLocation = playerLocation;
            lookLocation.height = playerLocation.height + 1;
            lookLocation.width = playerLocation.width + 1;
        }

        public void addLocation () {
            //TODO implement adding specific locations to the map
        }

        public void initializeSettings () {
//        this.window.setPreferredSize(new Dimension(1280,720));
//        this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//(JFrame.EXIT_ON_CLOSE);
//        this.window.getContentPane(); //why do this?
            this.setLayout(null);
//        this.window.add(panel);
//        this.window.setSize(1280,720);
//        this.window.setVisible(true);

            for (int i = 0; i < mapHeight; i++) {
                JLabel newLabel = new JLabel();
                newLabel.setSize((int) MAP_SCREEN_SIZE.getWidth(), VERTICAL_SPACING);
                newLabel.setBounds(300, i * VERTICAL_SPACING, (int) MAP_SCREEN_SIZE.getWidth(), VERTICAL_SPACING);
                rows.add(newLabel);
                add(newLabel);
            }

            this.addKeyListener(new ArrowKeyListener());
        }

        public boolean checkPosition () {
            Random encounterRand = new Random();
            char mapChar = map[(int) playerLocation.getWidth()][(int) playerLocation.getHeight()];
//            newStoryEvent(); //FIXME delete

            if ( goalCoordinates != null && (int) playerLocation.getHeight() == (int) goalCoordinates.getHeight() && (int) playerLocation.getWidth() == (int) goalCoordinates.getWidth()) {
                System.out.println("Congratulations! You found the goal!");
                newStoryEvent();
                //TODO Make the game window do it's thing here
            } else if ((mapChar == '/') || encounterRand.nextInt(15) < 2) {
                System.out.println("Fight!");
                map[(int) playerLocation.getWidth()][(int) playerLocation.getHeight()] = '%';
                if (gameWindow != null) {
                    gameWindow.randomEncounter();
                }
                return true;
            }


            return false;
        }

        public void setGoalCoordinates ( int x, int y){
            if (x < 0 || x >= mapWidth || y < 0 || y >= mapHeight) {
                return;
            }

            goalCoordinates = new Dimension(x, y);
            drawMap();
        }

        public void setPlayerLocation ( int x, int y){
            if (x < 0 || x >= mapWidth || y < 0 || y >= mapHeight) {
                return;
            }

            playerLocation = new Dimension(x, y);
        }

        private void newStoryEvent() {
            List<StoryNode> childrenList = new ArrayList<>();
            List<StoryNode> choices = new ArrayList<>();

            StoryNode firstNode = new StoryNode(null, null, "Congratulations! You've made it to your destination, Destinationville! What will you do here?", null);

            StoryNode choice1 = new StoryNode(null, null, "You walk until your legs hurt", "Walk");
            StoryNode child1 = new StoryNode(null, null, "You walk further anyways. Now you're feet hurt too", "Walk");
            childrenList.add(new StoryNode(null, null, "You continue walking. Your legs fall off ):", null, "Walk"));
            childrenList.add(new StoryNode(null, null, "Uh oh, you can't do that anymore. too bad", null, "Stop walking"));
            childrenList.add(new StoryNode());
            childrenList.add(new StoryNode());

            child1.setChoices(childrenList);

            choices.add(choice1);

            firstNode.setChoices(choices);

            gameWindow.setCurrentNode(firstNode);
            gameWindow.storyWindow();
        }

//    public void dispose() {
//        window.dispose();
//    }

        public KeyListener getKeyListener() {
            return new ArrowKeyListener();
        }


    public class ArrowKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            System.out.println("Registered key released");
            if (e.getKeyCode() == 38) {
                moveVertically(true);
            }
            if (e.getKeyCode() == 40) {
                moveVertically(false);
            }
            if (e.getKeyCode() == 37) {
                moveHorizontally(true);
            }
            if (e.getKeyCode() == 39) {
                moveHorizontally(false);
            }

            checkPosition();

//            System.out.println("Player location = " + playerLocation.toString()); //FIXME

        }
    }

}
