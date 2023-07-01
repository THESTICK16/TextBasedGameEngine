import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameWindow {
    private JFrame window;
    private ArrayList<JButton> choiceButtons;

    private JTextArea currentText;
    private JPanel panel;

    //added
    private Font textFont;

    private String gameState, battleState, curTitle;
    //String for all the text changes for the battle states
    // Goes start state, fight state, than talk stated
    private static final String[] battleStateArray = {"Fight", "Talk", "Items", "Run", "Melee", "Ranged", "Magic", "Back", "Chat", "Persuade", "Insult", "Back"};

    private static final String[] inventoryOptionArray = {"Weapons", "Armor", "Potions", "Food"};

    private HashMap<String, ArrayList<Integer>> weaponsList, armorList, potionList, foodList, storage;

    private int optionPos;

    private boolean canEquip, canUse;

    private JPanel top, bottom, left, right, center, buttonPanel, spacing3, statsPanel;

    private JLabel hpDisplayP, hpDisplayE, playerGold, strengthNum, magicNum, dexterityNum, armorNum, moraleDisplay;

    private ArrayList<JLabel> choiceLabels;

    private Player player;
    private Player enemy;
    private int halfPlayerHP;
    private int halfEnemyHP;
    private StoryNode currentNode;

    private int choiceNum;

    private Map map;

    private int moraleChange;

    private ArrayList<Integer> lastItemsArr;

    private ArrayList<String> potionsUsedInBattleList;


    AudioPlayer fightMusic;

    JList<String> itemsList, equippedWeaponsAndPositions, equippedArmor;
    JPopupMenu useOptionMenu, unequipWeaponsMenu, unequipArmorMenu;

    DefaultListModel<String> curList, weaponAndPotionModel, armorModel;

    private int tempPersuasion;
    private int tempChat;
    private int tempInsult;

    public GameWindow() {
        window = new JFrame("A Text Based Game");

        choiceButtons = new ArrayList<>();

        currentText = new JTextArea();
        currentText.setEditable(false);
        currentText.setLineWrap(true);
        currentText.setWrapStyleWord(true);
        currentText.setHighlighter(null);

        this.panel = new JPanel();

        // Added
        gameState = "";
        battleState = "";
        curTitle = "";

        choiceLabels = new ArrayList<>();

        choiceNum = 0;
        map = new Map();
        map.gameWindow = this;

        top = new JPanel();
        bottom = new JPanel();
        left = new JPanel();
        right = new JPanel();
        center = new JPanel();

        player = new Player();

        player.setHp(20);
        player.setDamageBound(7);
        player.setStrength(30);
        player.setDexterity(6);
        player.setMagic(12);
        player.setArmorClass(10);
        player.addArmor(5);
        player.setMagicHits(0);
        player.setRangedHits(0);
        player.setMeleeHits(0);
        player.setNormalArmorClass(player.getArmorClass());
        halfPlayerHP = player.getHp() / 2;
        playerGold = new JLabel();

        weaponsList = new HashMap<>();
        armorList = new HashMap<>();
        potionList = new HashMap<>();
        foodList = new HashMap<>();

        storage = new HashMap<>();

        //FIXME: TEMP
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(1); //num of item
        arr.add(0); // item id number
        arr.add(5); // strength increase/decrease
        arr.add(-3); // dexterity increase/decrease
        arr.add(0); // magic increase/decrease
        arr.add(0); // armor increase/decrease
        weaponsList.put("Axe", arr);
        arr = new ArrayList<>();
        arr.add(2);
        arr.add(0);
        arr.add(3); // strength increase/decrease
        arr.add(2); // dexterity increase/decrease
        arr.add(0); // magic increase/decrease
        arr.add(0); // armor increase/decrease
        weaponsList.put("Sword", arr);
        arr = new ArrayList<>();
        arr.add(3);
        arr.add(0);
        arr.add(5); // strength increase/decrease
        arr.add(-1); // dexterity increase/decrease
        arr.add(0); // magic increase/decrease
        arr.add(0); // armor increase/decrease
        weaponsList.put("Mace", arr);
        arr = new ArrayList<>();
        arr.add(2);
        arr.add(1);
        arr.add(4); // strength increase/decrease
        arr.add(-3); // dexterity increase/decrease
        arr.add(0); // magic increase/decrease
        arr.add(0); // armor increase/decrease
        weaponsList.put("Shield", arr);
        arr = new ArrayList<>();
        arr.add(1);
        arr.add(2);
        arr.add(2); // strength increase/decrease
        arr.add(5); // dexterity increase/decrease
        arr.add(0); // magic increase/decrease
        arr.add(0); // armor increase/decrease
        weaponsList.put("Bow", arr);

        arr = new ArrayList<>();
        arr.add(1); // number of
        arr.add(0); //type
        arr.add(0); // strength increase/decrease
        arr.add(0); // dexterity increase/decrease
        arr.add(0); // magic increase/decrease
        arr.add(4); // armor increase/decrease
        armorList.put("Helmet", arr);
        arr = new ArrayList<>();
        arr.add(1);
        arr.add(1); //type
        arr.add(0); // strength increase/decrease
        arr.add(-2); // dexterity increase/decrease
        arr.add(0); // magic increase/decrease
        arr.add(8); // armor increase/decrease
        armorList.put("Chestplate", arr);
        arr = new ArrayList<>();
        arr.add(1);
        arr.add(2);
        arr.add(0); // strength increase/decrease
        arr.add(0); // dexterity increase/decrease
        arr.add(0); // magic increase/decrease
        arr.add(3); // armor increase/decrease
        armorList.put("Gloves", arr);
        arr = new ArrayList<>();
        arr.add(1);
        arr.add(3);
        arr.add(0); // strength increase/decrease
        arr.add(-1); // dexterity increase/decrease
        arr.add(0); // magic increase/decrease
        arr.add(4); // armor increase/decrease
        armorList.put("Pants", arr);
        arr = new ArrayList<>();
        arr.add(1);
        arr.add(4);
        arr.add(0); // strength increase/decrease
        arr.add(3); // dexterity increase/decrease
        arr.add(0); // magic increase/decrease
        arr.add(4); // armor increase/decrease
        armorList.put("Boots", arr);

        arr = new ArrayList<>();
        arr.add(4);
        arr.add(0); //0 = not timelimited | != 0 is what the time limit is
        arr.add(0); // what type of potion | 0 = healing | 1 = magic
        arr.add(5); // increase by how much
        arr.add(3); // item id for equipping
        potionList.put("Healing", arr);
        arr = new ArrayList<>();
        arr.add(2);
        arr.add(0);
        arr.add(1);
        arr.add(2);
        arr.add(3); // item id for equipping
        potionList.put("Magic", arr);

        arr = new ArrayList<>();
        arr.add(1);
        foodList.put("Bread", arr);
        arr = new ArrayList<>();
        arr.add(3);
        foodList.put("Baked Potato", arr);
        arr = new ArrayList<>();
        arr.add(2);
        foodList.put("Water", arr);
        arr = new ArrayList<>();
        arr.add(1);
        foodList.put("Meat", arr);


        weaponAndPotionModel = new DefaultListModel<>();
        weaponAndPotionModel.addElement(" ");
        weaponAndPotionModel.addElement(" ");
        weaponAndPotionModel.addElement(" ");
        weaponAndPotionModel.addElement(" ");
        weaponAndPotionModel.addElement(" ");
        weaponAndPotionModel.addElement(" ");

        armorModel = new DefaultListModel<>();
        armorModel.addElement(" ");
        armorModel.addElement(" ");
        armorModel.addElement(" ");
        armorModel.addElement(" ");
        armorModel.addElement(" ");

        lastItemsArr = new ArrayList<>();
        potionsUsedInBattleList = new ArrayList<>();


        // Creates new font
        try {
            InputStream myStream = new BufferedInputStream(new FileInputStream("src/PixelFont.ttf"));
            textFont = Font.createFont(Font.TRUETYPE_FONT, myStream).deriveFont(20f);

        } catch (IOException | FontFormatException e) {
            System.out.println(e);
            textFont = new Font("American Typewriter", Font.PLAIN, 30);
        }

        window.addKeyListener(new ArrowKeyListener2());

        fightMusic = new AudioPlayer();

        setUpStory();

    }

    public GameWindow(JFrame window, ArrayList<JButton> choiceButtons, JTextArea currentText){
        this.window = window;
        this.choiceButtons = choiceButtons;
        this.currentText = currentText;
        this.panel = new JPanel();
    }

    // Creates the borders for all windows
    private void createBorders() {

        this.window.setLayout((new BorderLayout()));

        top.setBackground(Color.BLACK);
        bottom.setBackground(Color.BLACK);
        left.setBackground(Color.BLACK);
        right.setBackground(Color.BLACK);
        this.center.setBackground(Color.BLACK);

        top.setPreferredSize(new Dimension(1280, 50));
        bottom.setPreferredSize(new Dimension(1280, 50));
        left.setPreferredSize(new Dimension(50, 720));
        right.setPreferredSize(new Dimension(50, 720));

        this.window.add(top, BorderLayout.NORTH);
        this.window.add(bottom, BorderLayout.SOUTH);
        this.window.add(left, BorderLayout.WEST);
        this.window.add(right, BorderLayout.EAST);
        this.window.add(this.center, BorderLayout.CENTER);

        this.window.setVisible(true);

    }

    // Creates the layout for all the windows
    private void allWindowLayout() {
        setCenter(new JPanel()); // Makes new JFrame for the game

        this.window.setPreferredSize(new Dimension(1280, 720));
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createBorders();

        this.center.setBackground(Color.BLACK);

        BoxLayout boxLayout = new BoxLayout(this.center, BoxLayout.Y_AXIS);
        this.center.setLayout(boxLayout);
        this.window.pack();

//        this.center.addKeyListener(new ArrowKeyListener2());
        if (fightMusic.isPlaying()) {
            fightMusic.stopAudio();
        }
    }

    // Creates the start window layout
    private void startWindowLayout() {
        allWindowLayout();

        JPanel bbPanel = new JPanel(new FlowLayout((FlowLayout.LEFT)));
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel();

        titlePanel.setBackground(Color.BLACK);
        titlePanel.setMaximumSize(new Dimension(450, 100));

        bbPanel.setBackground(Color.BLACK); // Pushes title panel down
        bbPanel.setMaximumSize(new Dimension(1200, 50));


        if (curTitle.equals("Settings") || curTitle.equals("Credits")) {
            bbPanel.add(backButtonSetUp());
        }


        title.setText(curTitle);
        title.setFont(textFont.deriveFont(40f));
        title.setForeground(Color.WHITE);

        titlePanel.add(title);

        this.center.add(bbPanel);
        this.center.add(titlePanel);

    }

    private JButton backButtonSetUp() {
        JButton backBut = new JButton();

        backBut.setText("<");
        backBut.setBackground(Color.BLACK);
        backBut.setForeground(Color.WHITE);
        backBut.setBorderPainted(false);
        backBut.setFont(textFont.deriveFont(20f));

        backBut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backBut.setForeground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backBut.setForeground(Color.WHITE);
            }

        });


        return backBut;
    }

    // ----------------START SCREEN WINDOW--------------------

    // Creates the start window
    public void startWindow() {

        curTitle = "Adventure";
        startWindowLayout();
        this.center.setVisible(true);


        //Button Panel
        JPanel buttonPanel = new JPanel();
        JPanel extra2 = new JPanel();

        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setMaximumSize(new Dimension(300, 300));
        extra2.setBackground(Color.BLACK);
        extra2.setMaximumSize(new Dimension(300, 150));

        this.center.add(extra2);

        //Buttons
        buttonPanel.setLayout(new GridLayout(6, 1)); //sets buttons dimensions

        choiceButtons.clear();
        for (int i = 0; i < 3; i++) {
            JButton curBut = new JButton();

            buttonStyle(curBut);

            switch (i) {
                case 0:
                    curBut.setText("Start");
                    break;
                case 1:
                    curBut.setText("Settings");
                    break;
                case 2:
                    curBut.setText("Credits");
                    break;
                default:
                    System.out.println("Error with start window buttons");
                    break;
            }

            buttonPanel.add(curBut);
            choiceButtons.add(curBut);

            //Extra Panels
            JPanel spacingButs = new JPanel();
            spacingButs.setBackground(Color.BLACK);
            buttonPanel.add(spacingButs);

        }

        startWindowButtonListeners();

        this.center.add(buttonPanel);

    }

    private void startWindowButtonListeners() {
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == choiceButtons.get(0)) { // For the fight button
                    System.out.println("Start Button Clicked!");
                    storyWindow();
                } else if (e.getSource() == choiceButtons.get(1)) { // For the talk button
                    System.out.println("Settings Button Clicked!");
                    settingsWindow();
                } else if (e.getSource() == choiceButtons.get(2)) { // For the item button
                    System.out.println("Credit Button Clicked!");
                    creditsWindow();
                }
            }
        };

        for (int i = 0; i < 3; i++) {
            choiceButtons.get(i).addActionListener(buttonListener);
        }
    }


    // ----------------SETTINGS WINDOW--------------------

    //Settings Window Design
    private void settingsWindow() {

        curTitle = "Settings";

        startWindowLayout();

        this.center.setVisible(true);

    }


    // ----------------CREDITS WINDOW--------------------

    //Credit Window Design
    private void creditsWindow() {

        curTitle = "Credits";

        String[] names = {"James (Mentor)", "Ehukai", "Eric", "Ryan", "Tyler", "Ulysses"};

        startWindowLayout();

        JPanel creditContentPanel = new JPanel(new GridLayout(1, 2));
        JPanel picturePanel = new JPanel();
        JPanel ourTeamPanel = new JPanel();
        JLabel ourTeamText = new JLabel();

        ourTeamPanel.setLayout(new GridLayout(7, 1));

        ourTeamPanel.setBackground(Color.BLACK);
        picturePanel.setBackground(Color.BLACK);

        ourTeamText.setText("Our Team:");
        ourTeamText.setFont(textFont.deriveFont(25f));
        ourTeamText.setForeground(Color.WHITE);


        ourTeamPanel.add(ourTeamText);

        // Loops to add names
        for (int i = 0; i < 6; i++) {
            JLabel curName = new JLabel();
            curName.setForeground(Color.WHITE);
            curName.setFont(textFont.deriveFont(20f));
            curName.setText(names[i]);

            ourTeamPanel.add(curName);
        }

        // Adds Image to Picture Panel
        Image image = null;
        try {
            image = ImageIO.read(getClass().getResource("meme.png"));
            image = image.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            ImageIcon imageMeme = new ImageIcon(image);
            JLabel meme = new JLabel(imageMeme);
            picturePanel.add(meme);
        } catch (IOException e) {
            System.out.println(e);
        }

        creditContentPanel.add(ourTeamPanel);
        creditContentPanel.add(picturePanel);

        this.center.add(creditContentPanel);
        this.center.setVisible(true);

    }


    // ----------------STORY WINDOW--------------------

    public void storyWindow() {
        this.center.setVisible(false);
        allWindowLayout();
        choiceLabels.clear();

        gameState = "story";

        // Text Panel
        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.BLACK);
        textPanel.setMaximumSize(new Dimension(1220, 250));
        textPanel.setLayout(null);

        Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
        textPanel.setBorder(whiteLine);

        // Story Text
        currentText.setBackground(Color.BLACK);
        currentText.setForeground(Color.GREEN);
        currentText.setFont(textFont.deriveFont(15f));
        currentText.setBounds(30, 50, 1120, 150);
        currentText.setText(currentNode.getStoryText());

        textPanel.add(currentText);

        // Spacing Panel
        JPanel spacing = new JPanel();
        spacing.setBackground(Color.BLACK);
        spacing.setMaximumSize(new Dimension(50, 30));

        // Choice List Panel
        JPanel choiceListPanel = new JPanel();
        choiceListPanel.setBackground(Color.BLACK);
        choiceListPanel.setMaximumSize(new Dimension(1220, 150));
        choiceListPanel.setLayout(new BorderLayout());

        // Number Panel
        JPanel numPanel = new JPanel();
        numPanel.setBackground(Color.BLACK);
        numPanel.setMaximumSize(new Dimension(50, 150));
        numPanel.setLayout(new GridLayout(4, 1));

        // Choice Panel
        JPanel choicePanel = new JPanel();
        choicePanel.setBackground(Color.BLACK);
        choicePanel.setMaximumSize(new Dimension(1170, 150));
        choicePanel.setLayout(new GridLayout(4, 1));

        // Loops to add starting choices to choices array list
        ArrayList<String> choices = new ArrayList<>();
        if(this.currentNode.getChoices() != null){
            for(StoryNode n: this.currentNode.getChoices()){
                choices.add(n.getButtonText());
            }
        } else {
            for(JLabel j: this.choiceLabels) {
                j.setText("");
            }
        }

        // Number loop
        for (int i = 1; i < 5; i++) {
            JLabel num = new JLabel();
            num.setFont(textFont.deriveFont(17f));

            // Makes Colors
            switch (i) {
                case 1:
                    num.setForeground(Color.decode("#c39838"));
                    break;
                case 2:
                    num.setForeground(Color.decode("#28c6d7"));
                    break;
                case 3:
                    num.setForeground(Color.decode("#9460de"));
                    break;
                case 4:
                    num.setForeground(Color.decode("#59a668"));
                    break;
                default:
                    System.out.println("error in colors");
                    break;
            }

            num.setText("[" + i + "] ");

            numPanel.add(num);
        }

        choiceListPanel.add(numPanel, BorderLayout.WEST);

        // Choices loop
        for(int i = 0; i < choices.size(); i++){
            JLabel curChoice = new JLabel();

            curChoice.setFont(textFont.deriveFont(15f));
            curChoice.setForeground(Color.WHITE);

            curChoice.setText(choices.get(i));

            choicePanel.add(curChoice);
            choiceLabels.add(curChoice); //so we can change choice text later*/
        }

        choiceListPanel.add(choicePanel);

        // Spacing Panel 2
        JPanel spacing2 = new JPanel();
        spacing2.setBackground(Color.BLACK);
        spacing2.setMaximumSize(new Dimension(50, 20));

        // Choice button Panel
        styleBattleButtonPanel(1000, 60);
        buttonPanel.setLayout(new GridLayout(1, 4));

        JPanel buttonSpacing = new JPanel();
        buttonSpacing.setBackground(Color.BLACK);
        buttonPanel.add(buttonSpacing);

        // Setting up default buttons
        choiceButtons.clear(); // Makes it so we can restart the simulator
        for (int i = 1; i < 5; i++) {
            JButton curBut = new JButton();
            JPanel buttonSpacing2 = new JPanel();
            buttonSpacing2.setBackground(Color.BLACK);

            buttonStyle(curBut);
            Color curColor = Color.WHITE;

            switch(i) {
                case 1:
                    curColor = Color.decode("#c39838");
                    break;
                case 2:
                    curColor = Color.decode("#28c6d7");
                    break;
                case 3:
                    curColor = Color.decode("#9460de");
                    break;
                case 4:
                    curColor = Color.decode("#59a668");
                    break;
                default:
                    System.out.println("error in colors");
                    break;
            }
            curBut.setForeground(curColor);
            curBut.setBorder(BorderFactory.createLineBorder(curColor));

            curBut.setText(Integer.toString(i));

            buttonPanel.add(curBut);
            buttonPanel.add(buttonSpacing2);
            choiceButtons.add(curBut);
        }

        // Spacing
        JPanel spacing3 = new JPanel();
        spacing3.setBackground(Color.BLACK);
        spacing3.setMaximumSize(new Dimension(20, 20));

        // Map and Inventory Panel
        JPanel mapAndInvPanel = new JPanel();
        mapAndInvPanel.setBackground(Color.BLACK);
        mapAndInvPanel.setMaximumSize(new Dimension(1220, 60));
        mapAndInvPanel.setLayout((new BorderLayout()));

        // Map and Inventory Buttons
        JButton mapBut = new JButton();
        JButton invBut = new JButton();
        buttonStyle(mapBut);
        buttonStyle(invBut);

        mapBut.setText("Map");
        invBut.setText("Items");

        mapBut.setPreferredSize(new Dimension(150, 40));
        invBut.setPreferredSize(new Dimension(150, 40));

        mapBut.setEnabled(false);

        mapAndInvPanel.add(mapBut, BorderLayout.WEST);
        mapAndInvPanel.add(invBut, BorderLayout.EAST);

        choiceButtons.add(mapBut);
        choiceButtons.add(invBut);

        // TEMPORARY
        JButton restartSim = new JButton();
        buttonStyle(restartSim);
        restartSim.setText("Restart");
        restartSim.setPreferredSize(new Dimension(100, 40));
        mapAndInvPanel.add(restartSim, BorderLayout.CENTER);
        choiceButtons.add(restartSim);

        disableUnusedButtons();
        choiceButtonListener();
        mapAndItemButtonListeners();

        this.center.add(textPanel);
        this.center.add(spacing);
        this.center.add(choiceListPanel);
        this.center.add(spacing2);
        this.center.add(buttonPanel);
        this.center.add(spacing3);
        this.center.add(mapAndInvPanel);
        this.center.setVisible(true);
    }

    // If choice not available for that button
    private void disableUnusedButtons() {
        List<StoryNode> curChoices = this.currentNode.getChoices();
        if(curChoices != null) {
            for(int i = 0; i < curChoices.size(); i++) {
                if(curChoices.get(i).getButtonText() != null) {
                    choiceButtons.get(i).setEnabled(true);
                } else {
                    choiceButtons.get(i).setEnabled(false);
                }
            }
        }
    }

    public void updateText() {
        currentText.setText(currentNode.getStoryText());

        if(this.currentNode.getMapGoal() != null) {
            choiceButtons.get(4).setEnabled(true);
        }
    }

    public void updateChoices(){
        if(this.currentNode.getChoices() != null){
            JLabel curChoiceLabel;
            for(int i = 0; i < this.currentNode.getChoices().size(); i ++){
                StoryNode n = this.currentNode.getChoices().get(i);
                curChoiceLabel = this.choiceLabels.get(i);
                curChoiceLabel.setText(n.getButtonText());

            }
        }
        else{
            for(JLabel j: this.choiceLabels){
                j.setText("");
            }
        }
    }

    public void updateStats() {
        int upStrength = this.currentNode.getUpStrength() + this.player.getMeleeHits();
        int upMagic = this.currentNode.getUpMagic() + this.player.getMagicHits();
        int upDexterity = this.currentNode.getUpDexterity() + this.player.getRangedHits();
        int upArmorClass = this.currentNode.getUpArmorClass();

        player.setStrength(player.getStrength() + upStrength);
        player.setMagic(player.getMagic() + upMagic);
        player.setDexterity(player.getDexterity() + upDexterity);
        player.setArmorClass(player.getArmorClass() + upArmorClass);
    }

    // Action listener for 1-4 choice buttons
    public void choiceButtonListener(){
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == choiceButtons.get(0)){ // For the first choice button
                    System.out.println("1");
                    choiceNum = 0;

                } else if(e.getSource() == choiceButtons.get(1)){ // For the second choice button
                    System.out.println("2");
                    choiceNum = 1;

                } else if(e.getSource() == choiceButtons.get(2)){ // For the third choice button
                    System.out.println("3");
                    choiceNum = 2;

                } else if(e.getSource() == choiceButtons.get(3)){ // For the fourth choice button
                    System.out.println("4");
                    choiceNum = 3;

                }

                currentNode = currentNode.getChoices().get(choiceNum);
                if(currentNode.getEnemyEncounter() == null) {
                    updateText();
                    updateChoices();
                    disableUnusedButtons();
                    updateStats();

                    if(currentNode.getItemString() != null) {
                        updateItemList();
                        currentText.append("\n\nYou got " + currentNode.getItemArr().get(0) + " " + currentNode.getItemString() + " item.");
                    }
                }
                else{
                    // TODO: update text to say what was encountered
                    disableChoiceButtons();

                    currentText.setText(currentNode.getStoryText());
                    int delay = 2000;
                    Timer timer = new Timer( delay, ex -> {
                        enemy = currentNode.getEnemyEncounter();
                        fightWindow();
                    });
                    timer.setRepeats(false);
                    timer.start();

                }
            }
        };

        for(int i = 0; i < 4; i++) {
            choiceButtons.get(i).addActionListener(buttonListener);
        }
    }

    // Action listener for map and item button
    public void mapAndItemButtonListeners(){
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == choiceButtons.get(4)){ // For the run button
                    System.out.println("map");
                    mapWindow();

                } else if (e.getSource() == choiceButtons.get(5)) { // For the run button
                    System.out.println("Items");
                    itemWindow();
                } else if(e.getSource() == choiceButtons.get(6)) { //Temporary
                    GameWindow game = new GameWindow();
                    game.storyWindow();
                }
            }
        };

        for(int i = 4; i < 7; i++) {
            choiceButtons.get(i).addActionListener(buttonListener);
        }
    }


    // ---------------MAP WINDOW------------------------

    public void mapWindow() {
        allWindowLayout();

        JPanel backButPanel = new JPanel();
        backButPanel.setBackground(Color.BLACK);
        backButPanel.setMaximumSize(new Dimension(1220, 30));
        backButPanel.setLayout(new BorderLayout());

        backButPanel.add(backButtonSetUp(), BorderLayout.WEST);

        JPanel mapPanel = new JPanel();
        mapPanel.setBackground(Color.BLACK);
        mapPanel.setLayout(null);

        map.setPlayerLocation(this.currentNode.getMapGoal());
        map.addToPanel(mapPanel);
        map.loadMap();
        map.drawMap();


        this.center.add(backButPanel);
        this.center.add(mapPanel);
        mapPanel.grabFocus();
        this.center.setVisible(true);
    }

    // ---------------ITEMS WINDOW------------------------
    public void itemWindow() {
        allWindowLayout();
        canEquip = true;

        JPanel spacing = new JPanel();
        spacing.setBackground(Color.BLACK);
        spacing.setMaximumSize(new Dimension(50, 20));

        // Panel for inv type and left and right arrows
        JPanel arrowAndInvTypePanel = new JPanel();
        arrowAndInvTypePanel.setBackground(Color.BLACK);
        arrowAndInvTypePanel.setMaximumSize(new Dimension(1220, 50));
        arrowAndInvTypePanel.setLayout(new FlowLayout());

        // left arrow
        JButton leftArrow = new JButton();
        leftArrow.setText("<-");
        leftArrow.setBackground(Color.BLACK);
        leftArrow.setForeground(Color.WHITE);
        leftArrow.setBorderPainted(false);
        leftArrow.setFont(textFont.deriveFont(20f));

        arrowAndInvTypePanel.add(leftArrow);

        // inv type
        ArrayList<JLabel> invOptions = new ArrayList<>();
        for(int i = 0; i < inventoryOptionArray.length; i++) {
            JLabel curLabel = new JLabel();
            curLabel.setText(inventoryOptionArray[i]);
            if(i == 0) {
                curLabel.setFont(textFont.deriveFont(25f));
                curLabel.setForeground(Color.WHITE);
            } else {
                curLabel.setFont(textFont.deriveFont(20f));
                curLabel.setForeground(Color.GRAY);
            }
            invOptions.add(curLabel);
            arrowAndInvTypePanel.add(curLabel);
        }

        // right arrow
        JButton rightArrow = new JButton();
        rightArrow.setText("->");
        rightArrow.setBackground(Color.BLACK);
        rightArrow.setForeground(Color.WHITE);
        rightArrow.setBorderPainted(false);
        rightArrow.setFont(textFont.deriveFont(20f));

        arrowAndInvTypePanel.add(rightArrow);

        ((FlowLayout)arrowAndInvTypePanel.getLayout()).setHgap(50); //Changes gap size between labels

            // Spacing 2
        JPanel spacing2 = new JPanel();
        spacing2.setBackground(Color.BLACK);
        spacing2.setMaximumSize(new Dimension(50, 50));

        optionPos = 0;
        invArrowListeners(leftArrow, rightArrow, invOptions);
        invArrowHoverListener(leftArrow, rightArrow);

        // Panel inventory panel
        JPanel invPanel = new JPanel();
        invPanel.setBackground(Color.BLACK);
        invPanel.setMaximumSize(new Dimension(1220, 600));
        BoxLayout boxLayout = new BoxLayout(invPanel, BoxLayout.X_AXIS);
        invPanel.setLayout(boxLayout);

            //Item Panel
        JPanel itemsPanel = new JPanel();
        itemsPanel.setBackground(Color.BLACK);
        itemsPanel.setMaximumSize(new Dimension(500, 600));
        itemsPanel.setLayout(null);


            // Item list
        itemsList = new JList<>();
        itemsList.setBackground(Color.BLACK);
        itemsList.setForeground(Color.WHITE);
        itemsList.setFont(textFont.deriveFont(17f));
        itemsList.setFixedCellHeight(40);

        displayCurItemList();

        // Scroll Pane
            // use CustomScrollBarUI?
        JScrollPane scrollPane = new JScrollPane(itemsList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.setBackground(Color.BLACK);
        scrollPane.setBorder(null);
        scrollPane.setBounds(30, 30, 440, 430);

        itemsPanel.add(scrollPane);

        Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
        itemsPanel.setBorder(whiteLine);

        // ------------------------------------------------------------------------------------------------------------------------------

            // Equipped Panel
        JPanel equippedPanel = new JPanel();
        equippedPanel.setBackground(Color.BLACK);
        equippedPanel.setMaximumSize(new Dimension(600, 600));
        equippedPanel.setLayout(new BoxLayout(equippedPanel, BoxLayout.Y_AXIS));

        // spacing3 panel
        JPanel spacing3 = new JPanel();
        spacing3.setBackground(Color.BLACK);
        spacing3.setMaximumSize(new Dimension(10, 30));


            // HP and Gold Panel
        JPanel hpAndGoldPanel = new JPanel();
        hpAndGoldPanel.setBackground(Color.BLACK);
        hpAndGoldPanel.setMaximumSize(new Dimension(570, 30));
        hpAndGoldPanel.setLayout(new BoxLayout(hpAndGoldPanel, BoxLayout.X_AXIS));

            // HP Panel
        JPanel hpPanel = new JPanel();
        hpPanel.setBackground(Color.BLACK);
        hpPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            // itemWindow HP display
        JLabel hpSpot = new JLabel();
        hpSpot.setForeground(Color.WHITE);
        hpSpot.setFont(textFont.deriveFont(12f));
        hpSpot.setText("HP:");

            // hp
        hpDisplayP = new JLabel();
        hpDisplayP.setFont(textFont.deriveFont(12f));
        hpDisplayP.setText("" + player.getHp());
        healthColorChange();

        hpPanel.add(hpSpot);
        hpPanel.add(hpDisplayP);

            // gold Panel
        JPanel goldPanel = new JPanel();
        goldPanel.setBackground(Color.BLACK);
        goldPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

            // itemWindow gold display
        JLabel goldSpot = new JLabel();
        goldSpot.setForeground(Color.WHITE);
        goldSpot.setFont(textFont.deriveFont(12f));
        goldSpot.setText("Gold:");

            // gold
        playerGold.setForeground(Color.decode("#D4AF37"));
        playerGold.setFont(textFont.deriveFont(12f));
        playerGold.setText("" + player.getGold());

        goldPanel.add(goldSpot);
        goldPanel.add(playerGold);

        hpAndGoldPanel.add(hpPanel);
        hpAndGoldPanel.add(goldPanel);


            // equipped items panel
        JPanel equippedItemsPanel = new JPanel();
        equippedItemsPanel.setBackground(Color.BLACK);
        equippedItemsPanel.setMaximumSize(new Dimension(570, 390)); //was 350
        equippedItemsPanel.setLayout(new BoxLayout(equippedItemsPanel, BoxLayout.X_AXIS));

            // armor panel
        JPanel armorPanel = new JPanel();
        armorPanel.setBackground(Color.BLACK);
        armorPanel.setLayout(null);

        equippedArmor = new JList<>();
        equippedArmor.setBackground(Color.BLACK);
        equippedArmor.setForeground(Color.WHITE);
        equippedArmor.setFont(textFont.deriveFont(15f));
        equippedArmor.setBounds(10, 10, 250, 150);
        equippedArmor.setBorder(whiteLine);
        equippedArmor.setFixedCellHeight(30);

        equippedArmor.setModel(armorModel);
        armorPanel.add(equippedArmor);

        // weapons and potions panel
        JPanel weaponsAndPotionsPanel = new JPanel();
        weaponsAndPotionsPanel.setBackground(Color.BLACK);
        weaponsAndPotionsPanel.setLayout(null);

        // Jlist of equipped weapons and potions
        equippedWeaponsAndPositions = new JList<>();
        equippedWeaponsAndPositions.setBackground(Color.BLACK);
        equippedWeaponsAndPositions.setForeground(Color.WHITE);
        equippedWeaponsAndPositions.setFont(textFont.deriveFont(15f));
        equippedWeaponsAndPositions.setBounds(10, 10, 230, 180); // was 120
        equippedWeaponsAndPositions.setBorder(whiteLine);
        equippedWeaponsAndPositions.setFixedCellHeight(30);

        equippedWeaponsAndPositions.setModel(weaponAndPotionModel);
        weaponsAndPotionsPanel.add(equippedWeaponsAndPositions);


            // panel for stat display
        JPanel statDisplayPanel = new JPanel();
        statDisplayPanel.setBackground(Color.BLACK);
        statDisplayPanel.setBounds(25, 180, 230, 200); // y: 150
        statDisplayPanel.setLayout(new BoxLayout(statDisplayPanel, BoxLayout.Y_AXIS));

        JPanel statsTitlePanel = new JPanel();
        statsTitlePanel.setBackground(Color.BLACK);
        statsTitlePanel.setMaximumSize(new Dimension(100, 20));

            // stats title
        JLabel statsTitle = new JLabel();
        statsTitle.setForeground(Color.WHITE);
        statsTitle.setFont(textFont.deriveFont(15f));
        statsTitle.setText("Stats:");

        statsTitlePanel.add(statsTitle);

            // stat list panel
        JPanel statListPanel = new JPanel();
        statListPanel.setBackground(Color.BLACK);
        statListPanel.setFont(textFont.deriveFont(12f));
        statListPanel.setLayout(new GridBagLayout());
        statListPanel.setMaximumSize(new Dimension(230, 270));

            // stat numbers
        strengthNum = new JLabel();
        magicNum = new JLabel();
        dexterityNum = new JLabel();
        armorNum = new JLabel();

        strengthNum.setFont(textFont.deriveFont(12f));
        strengthNum.setForeground(Color.RED);
        strengthNum.setText("" + player.getStrength());

        magicNum.setFont(textFont.deriveFont(12f));
        magicNum.setForeground(Color.CYAN);
        magicNum.setText("" + player.getMagic());

        dexterityNum.setFont(textFont.deriveFont(12f));
        dexterityNum.setForeground(Color.decode("#b5651d"));
        dexterityNum.setText("" + player.getDexterity());

        armorNum.setFont(textFont.deriveFont(12f));
        armorNum.setForeground(Color.LIGHT_GRAY);
        armorNum.setText("" + player.getArmorClass());

        statListStyle(statListPanel);

        statDisplayPanel.add(statsTitlePanel);
        statDisplayPanel.add(statListPanel);

        //weaponsAndPotionsPanel.add(statDisplayPanel);
        armorPanel.add(statDisplayPanel);

        equippedItemsPanel.add(armorPanel);
        equippedItemsPanel.add(weaponsAndPotionsPanel);

        equippedPanel.add(spacing3);
        equippedPanel.add(hpAndGoldPanel);
        equippedPanel.add(equippedItemsPanel);

        createUnequipWeaponsMenu();
        createUnequipArmorMenu();

        invPanel.add(itemsPanel);
        invPanel.add(equippedPanel);

        this.center.add(spacing);
        this.center.add(arrowAndInvTypePanel);
        this.center.add(spacing2);
        this.center.add(invPanel);
        this.center.setVisible(true);
    }

    // Styles the stat display columns
    private void statListStyle(JPanel statsPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        for(int i = 0; i < 4; i++) {
            JLabel curLabel = new JLabel();
            curLabel.setForeground(Color.WHITE);
            curLabel.setFont(textFont.deriveFont(12f));
            gbc.weighty = 0.5;
            gbc.weightx = 0.2;
            gbc.anchor = GridBagConstraints.WEST;

            switch(i) {
                case 0:
                    curLabel.setText("Strength: ");
                    statsPanel.add(curLabel, gbc);

                    gbc.gridx = 1;
                    gbc.gridy = 0;
                    statsPanel.add(strengthNum, gbc);
                    break;
                case 1:
                    curLabel.setText("Magic: ");
                    gbc.gridx = 0;
                    gbc.gridy = 1;
                    statsPanel.add(curLabel, gbc);

                    gbc.gridx = 1;
                    gbc.gridy = 1;
                    statsPanel.add(magicNum, gbc);
                    break;
                case 2:
                    curLabel.setText("Dexterity: ");
                    gbc.gridx = 0;
                    gbc.gridy = 2;
                    statsPanel.add(curLabel, gbc);

                    gbc.gridx = 1;
                    gbc.gridy = 2;
                    statsPanel.add(dexterityNum, gbc);
                    break;
                case 3:
                    curLabel.setText("Armor: ");
                    gbc.gridx = 0;
                    gbc.gridy = 3;
                    statsPanel.add(curLabel, gbc);

                    gbc.gridx = 1;
                    gbc.gridy = 3;
                    statsPanel.add(armorNum, gbc);
                    break;
                default:
                    break;

            }

        }

    }


    // Loops through current screens map and displays current item list
    private void displayCurItemList() {
        HashMap<String, ArrayList<Integer>> curMap = new HashMap<>();
        curList = new DefaultListModel<>();

        switch(optionPos) {
            case 0:
                curMap = weaponsList;
                break;
            case 1:
                curMap = armorList;
                break;
            case 2:
                curMap = potionList;
                break;
            case 3:
                curMap = foodList;
                break;
            default:
                System.out.println("error with displayCurItemList switch");
                break;
        }

        for(HashMap.Entry<String, ArrayList<Integer>> entry : curMap.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue().get(0);
            curList.addElement(key + "     x" + value);
        }

        itemsList.setModel(curList);
        createPopupMenu();
    }


    // creates popup menu for itemlist
    private void createPopupMenu() {
        useOptionMenu = new JPopupMenu();
        useOptionMenu.setLayout(null);
        Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
        useOptionMenu.setBorder(whiteLine);

        useOptionMenu.setBackground(Color.DARK_GRAY);

        JMenuItem equip = new JMenuItem("Equip");
        JMenuItem use = new JMenuItem("Use");
        JMenuItem drop = new JMenuItem("Drop");

        popupStylingForOption(equip);
        useOptionMenu.addSeparator();
        popupStylingForOption(use);
        useOptionMenu.addSeparator();
        popupStylingForOption(drop);

        if(canEquip) {
            equip.setEnabled(true);
        } else {
            equip.setEnabled(false);
        }

        if(canUse) {
            use.setEnabled(true);
        } else {
            use.setEnabled(false);
        }

        itemsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                check(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                check(e);
            }

            public void check(MouseEvent e) {
                if(e.isPopupTrigger()) {
                    itemsList.setSelectedIndex(itemsList.locationToIndex(e.getPoint()));
                    useOptionMenu.show(itemsList, e.getX() + 40, e.getY());
                }
            }
        });

        // displays item info when hovering over it
        itemsList.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                JList l = (JList) e.getSource();
                ListModel m = l.getModel();
                int index = l.locationToIndex(e.getPoint());

                UIManager.put("ToolTip.background", Color.DARK_GRAY);
                UIManager.put("ToolTip.foreground", Color.WHITE);
                UIManager.put("ToolTip.border", BorderFactory.createLineBorder(Color.GRAY));
                UIManager.put("ToolTip.font", new Font("Monospaced", Font.BOLD, 12));

                if(index > -1) {
                    String curItem = cleanUpString(m.getElementAt(index).toString());
                    if(optionPos == 0) {
                        ArrayList<Integer> arr = weaponsList.get(curItem);
                        l.setToolTipText(
                                "<html>Strength: " + arr.get(2) + "<br>" +
                                "Magic: " + arr.get(3) + "<br>" +
                                "Dexterity: " + arr.get(4) + "<br>" +
                                "Armor: " + arr.get(5) + "</html>"
                        );
                    } else if(optionPos == 1) {
                        ArrayList<Integer> arr = armorList.get(curItem);
                        l.setToolTipText(
                                "<html>Strength: " + arr.get(2) + "<br>" +
                                        "Magic: " + arr.get(3) + "<br>" +
                                        "Dexterity: " + arr.get(4) + "<br>" +
                                        "Armor: " + arr.get(5) + "</html>"
                        );
                    } else if(optionPos == 2) {
                        ArrayList arr = potionList.get(curItem);

                        if(arr.get(1).equals(0)) {
                            if(arr.get(2).equals(0)) {
                                l.setToolTipText(
                                        "<html>Health Increase: " + arr.get(3) + "<br>" +
                                                "TimeLimit: none</html>"
                                );
                            } else if(arr.get(2).equals(1)) {
                                l.setToolTipText(
                                        "<html>Magic Increase: " + arr.get(3) + "<br>" +
                                                "TimeLimit: none</html>"
                                );
                            }

                        } else {
                            if(arr.get(2).equals(0)) {
                                l.setToolTipText(
                                        "<html>Health Increase: " + arr.get(3) + "<br>" +
                                                "TimeLimit: " + arr.get(1) + "</html>"
                                );
                            } else if(arr.get(2).equals(1)) {
                                l.setToolTipText(
                                        "<html>Magic Increase: " + arr.get(3) + "<br>" +
                                                "TimeLimit: " + arr.get(1) + "</html>"
                                );
                            }
                        }
                    } else if(optionPos == 3) {
                        ArrayList arr = foodList.get(curItem);

                        l.setToolTipText("Yum! x" + arr.get(0));
                    }
                }
            }
        });

        popupMenuListener(drop, use, equip);
    }


    // Creates popup menu for unequipping weapons and potions
    private void createUnequipWeaponsMenu() {
        unequipWeaponsMenu = new JPopupMenu();
        unequipWeaponsMenu.setLayout(null);
        Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
        unequipWeaponsMenu.setBorder(whiteLine);

        unequipWeaponsMenu.setBackground(Color.DARK_GRAY);

        JMenuItem unequip = new JMenuItem("Unequip");

        popupStylingForUnequipWeapons(unequip);

        equippedWeaponsAndPositions.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                check(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                check(e);
            }

            public void check(MouseEvent e) {
                if(e.isPopupTrigger()) {
                    if(weaponAndPotionModel.get(equippedWeaponsAndPositions.getSelectedIndex()) != " ") {
                        equippedWeaponsAndPositions.setSelectedIndex(equippedWeaponsAndPositions.locationToIndex(e.getPoint()));
                        unequipWeaponsMenu.show(equippedWeaponsAndPositions, e.getX() + 40, e.getY());
                    }
                }
            }
        });


        unequipWeaponMenuListener(unequip);
    }

    // Action listener for unequipping weapons
    private void unequipWeaponMenuListener(JMenuItem unequip) {
        unequip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int itemPos = equippedWeaponsAndPositions.getSelectedIndex();
                String curEquippedItem = weaponAndPotionModel.get(itemPos);
                ArrayList<Integer> curList;

                boolean isPotion = false;

                if(itemPos < 3) {
                    curList = weaponsList.get(curEquippedItem);
                    int numItems;
                    if(weaponsList.containsKey(curEquippedItem)) {
                        numItems = curList.get(0);
                        numItems++;
                        curList.set(0, numItems);
                        weaponAndPotionModel.set(itemPos, " ");
                    } else {
                        weaponsList.put(curEquippedItem, storage.get(curEquippedItem));
                        weaponAndPotionModel.set(itemPos, " ");
                    }
                } else {
                    curList = potionList.get(curEquippedItem);
                    int numItems;
                    if(potionList.containsKey(curEquippedItem)) {
                        numItems = curList.get(0);
                        numItems++;
                        curList.set(0, numItems);
                        weaponAndPotionModel.set(itemPos, " ");

                    } else {
                        potionList.put(curEquippedItem, storage.get(curEquippedItem));
                        weaponAndPotionModel.set(itemPos, " ");

                    }

                    isPotion = true;
                }


                if(isPotion == false) {
                    equippedChangeStats(" ", curEquippedItem);
                }

                storage.remove(curEquippedItem);
                displayCurItemList();
            }
        });
    }

    // Creates popup menu for unequipping armor
    private void createUnequipArmorMenu() {
        unequipArmorMenu = new JPopupMenu();
        unequipArmorMenu.setLayout(null);
        Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
        unequipArmorMenu.setBorder(whiteLine);

        unequipArmorMenu.setBackground(Color.DARK_GRAY);

        JMenuItem unequip = new JMenuItem("Unequip");

        popupStylingForUnequipArmor(unequip);

        equippedArmor.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                check(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                check(e);
            }

            public void check(MouseEvent e) {
                if(e.isPopupTrigger()) {
                    if(armorModel.get(equippedArmor.getSelectedIndex()) != " ") {
                        equippedArmor.setSelectedIndex(equippedArmor.locationToIndex(e.getPoint()));
                        unequipArmorMenu.show(equippedArmor, e.getX() + 40, e.getY());
                    }
                }
            }
        });

        unequipArmorMenuListener(unequip);
    }

    // Action listener for unequipping armor
    private void unequipArmorMenuListener(JMenuItem unequip) {
        unequip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int itemPos = equippedArmor.getSelectedIndex();
                String curEquippedItem = armorModel.get(itemPos);
                ArrayList<Integer> curList;

                int numItems;

                if(armorList.containsKey(curEquippedItem)) {
                    curList = armorList.get(curEquippedItem);
                    numItems = curList.get(0);
                    numItems++;
                    curList.set(0, numItems);
                    armorModel.set(itemPos, " ");
                } else {
                    armorList.put(curEquippedItem, storage.get(curEquippedItem));
                    armorModel.set(itemPos, " ");
                }

                equippedChangeStats(" ", curEquippedItem);

                storage.remove(curEquippedItem);
                displayCurItemList();
            }
        });
    }


    // styles options for the itemlist popup
    private void popupStylingForOption(JMenuItem curItem) {
        curItem.setBackground(Color.DARK_GRAY);
        curItem.setFont(textFont.deriveFont(15f));
        curItem.setForeground(Color.WHITE);

        useOptionMenu.add(curItem);
    }

    // styles unequip option for weapon and potion popup
    private void popupStylingForUnequipWeapons(JMenuItem curItem) {
        curItem.setBackground(Color.DARK_GRAY);
        curItem.setFont(textFont.deriveFont(15f));
        curItem.setForeground(Color.WHITE);

        unequipWeaponsMenu.add(curItem);
    }

    // styles unequip option for armor popup
    private void popupStylingForUnequipArmor(JMenuItem curItem) {
        curItem.setBackground(Color.DARK_GRAY);
        curItem.setFont(textFont.deriveFont(15f));
        curItem.setForeground(Color.WHITE);

        unequipArmorMenu.add(curItem);
    }

    //TODO: in future keep backup list of dropped items
    private void popupMenuListener(JMenuItem drop, JMenuItem use, JMenuItem equip) {

        // drop popup menu action listener
        drop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int posInList = itemsList.getSelectedIndex();
                String curItem = itemsList.getModel().getElementAt(posInList);
                curItem = cleanUpString(curItem);
                if(optionPos == 0) {
                    dropOrUseItem(weaponsList, curItem);
                } else if(optionPos == 1) {
                    dropOrUseItem(armorList, curItem);
                } else if(optionPos == 2) {
                    dropOrUseItem(potionList, curItem);
                } else if (optionPos == 3) {
                    dropOrUseItem(foodList, curItem);
                }
            }
        });

        // Use popup menu action listener
        use.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int posInList = itemsList.getSelectedIndex();
                String curItem = itemsList.getModel().getElementAt(posInList);
                curItem = cleanUpString(curItem);

                if(optionPos == 2) {
                    int timeLimit = potionList.get(curItem).get(1);
                    int type = potionList.get(curItem).get(2);
                    int addition = potionList.get(curItem).get(3);

                    if(timeLimit == 0) {
                        switch(type) {
                            case 0:
                                System.out.println(player.getHp());
                                int increase = player.getHp() + addition;
                                if((increase >= player.maxHP)) {
                                    player.setHp(player.maxHP);
                                    hpDisplayP.setText("" + player.getHp());
                                } else {
                                    player.setHp(increase);
                                    hpDisplayP.setText("" + player.getHp());
                                }
                                System.out.println(player.getHp());
                                break;
                            case 1:
                                System.out.println(player.getMagic());
                                player.setMagic(player.getMagic() + addition);
                                magicNum.setText("" + player.getMagic());
                                System.out.println(player.getMagic());
                                break;
                            default:
                                System.out.println("Error with USE action listener switch");
                        }
                    }

                    dropOrUseItem(potionList, curItem);

                } else if(optionPos == 3) {
                    dropOrUseItem(foodList, curItem);
                }
            }
        });


        // equips an item onto the player
        equip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int posInList = itemsList.getSelectedIndex();
                String curItem = itemsList.getModel().getElementAt(posInList);
                curItem = cleanUpString(curItem);

                if(optionPos == 0) {
                    changeEquippedArmor(weaponsList, curItem);
                } else if(optionPos == 1) {
                    changeEquippedArmor(armorList, curItem);
                } else if(optionPos == 2) {
                    changeEquippedArmor(potionList, curItem);
                }

            }
        });
    }

    // updates equipped armor when user clicks the equip button
    private void changeEquippedArmor(HashMap<String, ArrayList<Integer>> curMap, String curItem) {
        int type;
        String replacedItem = "";
        boolean isPotion = false;

        if(optionPos == 0) {
            type = curMap.get(curItem).get(1);
            replacedItem = weaponAndPotionModel.get(type);
            weaponAndPotionModel.set(type, curItem);
            storage.put(curItem, curMap.get(curItem));

        } else if(optionPos == 1) {
            type = curMap.get(curItem).get(1);
            replacedItem = armorModel.get(type);
            armorModel.set(type, curItem);
            storage.put(curItem, curMap.get(curItem));

        } else if(optionPos == 2) {
            type = curMap.get(curItem).get(4);

            // makes it so you can equip more than one potion
            int posClear = 0;

            if(weaponAndPotionModel.get(type) == " ") {
                posClear = 0;
            } else if(weaponAndPotionModel.get(type + 1) == " ") {
                posClear = 1;
            } else if(weaponAndPotionModel.get(type + 2) == " ") {
                posClear = 2;
            }

            type += posClear;

            replacedItem = weaponAndPotionModel.get(type);
            weaponAndPotionModel.set(type, curItem);
            storage.put(curItem, curMap.get(curItem));

            isPotion = true;
        }

        int numOfItem;
        if(replacedItem != " ") {
            if(curMap.containsKey(replacedItem)) {
                numOfItem = curMap.get(replacedItem).get(0);
                numOfItem++;
                curMap.get(replacedItem).set(0, numOfItem);
            } else {
                curMap.put(replacedItem, storage.get(replacedItem));
            }
        }

        storage.put(curItem, curMap.get(curItem));

        if(isPotion == false) {
            equippedChangeStats(curItem, replacedItem);
        }

        storage.remove(replacedItem);
        dropOrUseItem(curMap, curItem);

    }

    // Gets rid of items from the listed items that were dropped or used by the player
    private void dropOrUseItem(HashMap<String, ArrayList<Integer>> curMap, String curItem) {
        int numOfItem = curMap.get(curItem).get(0);
        numOfItem -= 1;
        if(numOfItem == 0) {
            curMap.remove(curItem);
        } else {
            curMap.get(curItem).set(0, numOfItem);
        }
        displayCurItemList();
    }

    // Gets the name of the current item so it can be searched in the HashMap
    private String cleanUpString(String curItem) {
        int numSpaces = 0;
        int subPos = 0;
        char curChar = ' ';
        for(int i = 0; i < curItem.length(); i++) {
            curChar = curItem.charAt(i);
            if(curChar == ' ') {
                numSpaces++;
                if(numSpaces == 2) {
                    subPos = i - 1;
                }
            } else {
                numSpaces = 0;
            }
        }
        curItem = curItem.substring(0, subPos);

        return curItem;
    }

    // changes stats based off what is equipped
    private void equippedChangeStats(String newEquipped, String replacedItem) {

        int newStrengthNum;
        int newMagicNum;
        int newDexterityNum;
        int newArmorNum;
        String num;

        if(newEquipped == " ") {
            ArrayList<Integer> curArr = storage.get(replacedItem);

            newStrengthNum = Integer.parseInt(strengthNum.getText()) - curArr.get(2);
            num = "" + newStrengthNum;
            strengthNum.setText(num);
            player.setStrength(newStrengthNum);

            newDexterityNum = Integer.parseInt(dexterityNum.getText()) - curArr.get(3);
            num = "" + newDexterityNum;
            dexterityNum.setText(num);
            player.setDexterity(newDexterityNum);

            newMagicNum = Integer.parseInt(magicNum.getText()) - curArr.get(4);
            num = "" + newMagicNum;
            magicNum.setText(num);
            player.setMagic(newMagicNum);

            newArmorNum = Integer.parseInt(armorNum.getText()) - curArr.get(5);
            num = "" + newArmorNum;
            armorNum.setText(num);
            player.setArmorClass(newArmorNum);

        } else {

            ArrayList<Integer> arr = storage.get(newEquipped);
            ArrayList<Integer> oldArr = storage.get(replacedItem);

            if(oldArr == null && replacedItem != " ") { // prevents error of incorrect stat changes
                oldArr = lastItemsArr;
            } else if(replacedItem == " ") {
                oldArr = new ArrayList<>();
                oldArr.add(0);
                oldArr.add(0);
                oldArr.add(0);
                oldArr.add(0);
                oldArr.add(0);
                oldArr.add(0);
            }

            if(arr != oldArr) {

                newStrengthNum = Integer.parseInt(strengthNum.getText()) + arr.get(2) - oldArr.get(2);
                num = "" + newStrengthNum;
                strengthNum.setText(num);
                player.setStrength(newStrengthNum);

                newDexterityNum = Integer.parseInt(dexterityNum.getText()) + arr.get(3) - oldArr.get(3);
                num = "" + newDexterityNum;
                dexterityNum.setText(num);
                player.setDexterity(newDexterityNum);

                newMagicNum = Integer.parseInt(magicNum.getText()) + arr.get(4) - oldArr.get(4);
                num = "" + newMagicNum;
                magicNum.setText(num);
                player.setMagic(newMagicNum);

                newArmorNum = Integer.parseInt(armorNum.getText()) + arr.get(5) - oldArr.get(5);
                num = "" + newArmorNum;
                armorNum.setText(num);
                player.setArmorClass(newArmorNum);

            }

            lastItemsArr = arr;

        }
    }


    // Allow for user to use, drop, or equip item
    //TODO: Allow for updating item list from story node
    private void updateItemList() {
        int type = this.currentNode.getItemType();
        if(type != -1) {
            if(type == 0) {
                checkIfInList(weaponsList);

            } else if(type == 1) {
                checkIfInList(armorList);

            } else if(type == 2) {
                checkIfInList(potionList);

            } else if(type == 3) {
                checkIfInList(foodList);
            }
        }

    }

    // checks the curList and updates item list
    public void checkIfInList(HashMap<String, ArrayList<Integer>> curList) {
        int temp;
        boolean theSame = false;

        if(curList.containsKey(this.currentNode.getItemString())){

            for(int i = 1; i < this.currentNode.getItemArr().size(); i++) {
                if(curList.get(this.currentNode.getItemString()).get(i) == this.currentNode.getItemArr().get(i)){
                    theSame = true;
                } else {
                    theSame = false;
                }
            }

            if(theSame == true) {
                temp = curList.get(this.currentNode.getItemString()).get(0) + this.currentNode.getItemArr().get(0);
                curList.get(this.currentNode.getItemString()).set(0, temp);
            } else {
                curList.put(this.currentNode.getItemString(), this.currentNode.getItemArr()); //TODO: make own map so can have same key but different values
            }

        } else {
            curList.put(this.currentNode.getItemString(), this.currentNode.getItemArr());
        }
    }

    private void checkCanEquip() {

        switch (optionPos) {
            case 0:
            case 1:
            case 2:
                canEquip = true;
                break;
            case 3:
                canEquip = false;
                break;
            default:
                System.out.println("Error with checkCanEquip method");
                break;
        }
    }

    private void checkCanUse() {
        switch(optionPos) {
            case 2:
            case 3:
                canUse = true;
                break;
            case 0:
            case 1:
                canUse = false;
                break;
            default:
                System.out.println("Error with checkCanUse method");
                break;
        }
    }

    // Action listener for arrows in items window
    private void invArrowListeners(JButton leftArrow, JButton rightArrow, ArrayList<JLabel> invOptions) {
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int previousPos = optionPos;
                if(e.getSource() == leftArrow) {
                    optionPos -= 1;
                    if(optionPos < 0) {
                        storyWindow();
                    } else {
                        JLabel curLabel = invOptions.get(optionPos);
                        curLabel.setForeground(Color.WHITE);
                        curLabel.setFont(textFont.deriveFont(25f));

                        JLabel prevLabel = invOptions.get(previousPos);
                        prevLabel.setForeground(Color.GRAY);
                        prevLabel.setFont(textFont.deriveFont(20f));
                    }
                } else if(e.getSource() == rightArrow) {
                    optionPos += 1;
                    if(optionPos > inventoryOptionArray.length - 1) {
                        optionPos = inventoryOptionArray.length - 1;
                    } else {
                        JLabel curLabel = invOptions.get(optionPos);
                        curLabel.setForeground(Color.WHITE);
                        curLabel.setFont(textFont.deriveFont(25f));

                        JLabel prevLabel = invOptions.get(previousPos);
                        prevLabel.setForeground(Color.GRAY);
                        prevLabel.setFont(textFont.deriveFont(20f));
                    }

                }
                checkCanEquip();
                checkCanUse();
                displayCurItemList();
            }

        };

        leftArrow.addActionListener(buttonListener);
        rightArrow.addActionListener(buttonListener);

    }


    private void invArrowHoverListener(JButton leftArrow, JButton rightArrow) {
        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (e.getSource() == leftArrow) {
                    leftArrow.setForeground(Color.GRAY);
                } else {
                    rightArrow.setForeground(Color.GRAY);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (e.getSource() == leftArrow) {
                    leftArrow.setForeground(Color.WHITE);
                } else {
                    rightArrow.setForeground(Color.WHITE);
                }
            }
        };

        leftArrow.addMouseListener(mouseListener);
        rightArrow.addMouseListener(mouseListener);
    }



    // ---------------FIGHT WINDOW------------------------
    // TODO: edit coloring on window

    // Creates fight window
    private void fightWindow() {
        allWindowLayout();
        gameState = "";



        // HP Panel
        JPanel hpPanel = new JPanel();
        hpPanel.setMaximumSize(new Dimension(800, 30));
        hpPanel.setBackground(Color.BLACK);

        //hp text display
        JLabel playerDisplay = new JLabel();
        hpDisplayP = new JLabel();
        JLabel enemyDisplay = new JLabel();
        hpDisplayE = new JLabel();
        moraleDisplay = new JLabel();
        JLabel moraleText = new JLabel();

        playerDisplay.setForeground(Color.WHITE);
        playerDisplay.setText("Player HP: ");
        hpDisplayP.setForeground(Color.decode("#74D306"));

        enemyDisplay.setForeground(Color.WHITE);
        enemyDisplay.setText("Enemy HP: ");
        hpDisplayE.setForeground(Color.decode("#74D306"));

        moraleDisplay.setForeground(Color.YELLOW);
        moraleText.setForeground(Color.WHITE);
        moraleText.setText("Morale: ");

        playerDisplay.setFont(textFont.deriveFont(15f));
        hpDisplayP.setFont(textFont.deriveFont(15f));
        enemyDisplay.setFont(textFont.deriveFont(15f));
        hpDisplayE.setFont(textFont.deriveFont(15f));
        moraleDisplay.setFont(textFont.deriveFont(15f));
        moraleText.setFont(textFont.deriveFont(15f));

        hpPanel.add(playerDisplay);
        hpPanel.add(hpDisplayP);
        hpPanel.add(moraleText);
        hpPanel.add(moraleDisplay);
        hpPanel.add(enemyDisplay);
        hpPanel.add(hpDisplayE);


        // Text Panel
        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.BLACK);
        textPanel.setMaximumSize(new Dimension(1220, 300));
        textPanel.setLayout(null);

        Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
        textPanel.setBorder(whiteLine);

        // Start Text
        currentText.setBackground(Color.BLACK);
        currentText.setForeground(Color.GREEN);
        currentText.setFont(textFont.deriveFont(15f));
        currentText.setBounds(30, 50, 1120, 200);

        textPanel.add(currentText);

        // Spacing Panel
        JPanel spacing = new JPanel();
        spacing.setBackground(Color.BLACK);
        spacing.setMaximumSize(new Dimension(100, 80));

        // Buttons
        styleBattleButtonPanel(1200, 80);
        buttonPanel.setLayout(new GridLayout(1, 4));

        JPanel buttonSpacing = new JPanel();
        buttonSpacing.setBackground(Color.BLACK);
        buttonPanel.add(buttonSpacing);

        // Setting up default buttons
        choiceButtons.clear(); // Makes it so we can restart the simulator
        for (int i = 0; i < 4; i++) {
            JButton curBut = new JButton();

            JPanel buttonSpacing2 = new JPanel();
            buttonSpacing2.setBackground(Color.BLACK);

            buttonStyle(curBut);

            buttonPanel.add(curBut);
            buttonPanel.add(buttonSpacing2);
            choiceButtons.add(curBut);
        }

        battleState = "start";
        enemy = currentNode.getEnemyEncounter();
        playerAndEnemySetup(); // Temporary spot
        updateBattleState();

        // Adds stats button
        statsPanel = new JPanel();
        spacing3 = new JPanel();

        spacing3.setBackground(Color.BLACK);
        spacing3.setMaximumSize(new Dimension(300, 40));

        statsPanel.setBackground(Color.BLACK);
        statsPanel.setMaximumSize(new Dimension(660, 80));
        statsPanel.setLayout(new GridLayout(1, 1));


        // Adds defend button

        statsPanel.setLayout(new GridLayout(1, 5));

        JButton defendButton = new JButton();
        buttonStyle(defendButton);
        defendButton.setText("Defend");
        defendButtonListener(defendButton);
        statsPanel.add(defendButton);

        JPanel spacing4 = new JPanel();
        spacing4.setBackground(Color.BLACK);

        JPanel spacing5 = new JPanel();
        spacing5.setBackground(Color.BLACK);

        JPanel spacing6 = new JPanel();
        spacing6.setBackground(Color.BLACK);

        statsPanel.add(spacing4);
        statsPanel.add(spacing5);
        statsPanel.add(spacing6);

        // Adds stats button
        JButton statsButton = new JButton();
        buttonStyle(statsButton);
        statsButton.setText("Stats");
        statsButtonListener(statsButton);
        statsButton.setMaximumSize(new Dimension(150, 80));
        statsPanel.add(statsButton);


        this.center.add(hpPanel);
        this.center.add(textPanel);

        this.center.add(spacing);
        this.center.add(buttonPanel);

        this.center.add(spacing3);
        this.center.add(statsPanel);

        this.center.setVisible(true);

        fightMusic.playAudio("src/110 Fighting.wav");
    }

    // Stat button Listener
    private void statsButtonListener(JButton statsButton) {
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentText.setText("Strength: " + player.getStrength() + "\n\n" + "Dexterity: " + player.getDexterity() + "\n\n"
                        + "Magic: " + player.getMagic() + "\n\n" + "Armor: " + player.getArmorClass() + "\n\n" + "Damage Bound: " + player.getDamageBound());
            }
        };
        statsButton.addActionListener(buttonListener);
    }

    // Updates the battle state and available buttons/options
    private void updateBattleState() {
        //if player health or enemy health is less than zero then exit

        if (battleState.equals("start")) {
            battleStateLoop(0);
            removeListeners();
            battleStartButtonListener();

        } else if (battleState.equals("fight")) {
            battleStateLoop(4);
            removeListeners();
            fightButtonListener();

        } else if (battleState.equals("talk")) {
            battleStateLoop(8);
            removeListeners();
            talkButtonListener();

        } else if(battleState.equals("item")) {

            for(int i = 3; i < 6; i++) {
                JButton curBut = choiceButtons.get(i - 3); // FIXME: need to remove from storage to prevent display but there is problem
                String curButText = weaponAndPotionModel.get(i);

                if(curButText == " ") {
                    curBut.setEnabled(false);
                    curBut.setText("---");
                } else {
                    curBut.setEnabled(true);
                    curBut.setText(curButText);
                }
            }

            choiceButtons.get(3).setText("Back");

            removeListeners();
            itemButtonListener();
        }

    }

    // loops through buttons and replaces them with the current battle state button text
    private void battleStateLoop(int arrPos) {
        int startPos = arrPos;

        for (int i = 0; i < 4; i++) {
            JButton curBut = choiceButtons.get(i);
            curBut.setEnabled(true);
            String curButText = battleStateArray[arrPos];
            curBut.setText(curButText);
            arrPos++;
        }
    }

    // Styles the battle button panel
    private void styleBattleButtonPanel(int width, int height) {
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setMaximumSize(new Dimension(width, height));
    }


    // Creates the button Listeners for the initial battle options
    private void battleStartButtonListener() {
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == choiceButtons.get(0)) { // For the fight button
                    battleState = "fight";
                    updateBattleState();

                } else if (e.getSource() == choiceButtons.get(1)) { // For the talk button
                    battleState = "talk";
                    updateBattleState();

                } else if (e.getSource() == choiceButtons.get(2)) { // For the item button
                    battleState = "item";
                    updateBattleState();

                } else if (e.getSource() == choiceButtons.get(3)) { // For the run button
                    battleState = "run";
                }
            }
        };

        for (int i = 0; i < 4; i++) {
            choiceButtons.get(i).addActionListener(buttonListener);
        }
    }

    // Creates the button listeners for the fight options
    private void fightButtonListener() {
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // For fight state
                if (e.getSource() == choiceButtons.get(0)) { //for Melee button
                    combatMec('p');
                    System.out.println("Melee button clicked");
                } else if (e.getSource() == choiceButtons.get(1)) { // for Ranged button
                    combatMec('r');
                    System.out.println("Ranged button clicked");

                } else if (e.getSource() == choiceButtons.get(2)) { // for Magic button
                    combatMec('m');
                    System.out.println("Magic button clicked");

                } else if (e.getSource() == choiceButtons.get(3)) { //for back button
                    battleState = "start";
                    updateBattleState();
                }
            }
        };
        for (int i = 0; i < 4; i++) {
            choiceButtons.get(i).addActionListener(buttonListener);
        }
    }

    //TODO: SET THIS UP!!

    // Item button listener
    private void itemButtonListener() {
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == choiceButtons.get(0)){ //for Melee button
                    System.out.println("potion 1");
                    usePotion(0);

                } else if(e.getSource() == choiceButtons.get(1)){ // for Ranged button
                    System.out.println("potion 2");
                    usePotion(1);

                } else if(e.getSource() == choiceButtons.get(2)){ // for Magic button
                    System.out.println("potion 3");
                    usePotion(2);

                } else if(e.getSource() == choiceButtons.get(3)){ //for back button
                    battleState = "start";
                    updateBattleState();
                }
            }
        };

        for(int i = 0; i < 4; i++) {
            choiceButtons.get(i).addActionListener(buttonListener);
        }
    }

    // Uses the given potion and makes sure the potion doesn't appear again
    private void usePotion(int potNum) {

        String curPotion = choiceButtons.get(potNum).getText();

        if(!(curPotion.equals("---"))) {
            ArrayList<Integer> arr = storage.get(curPotion);


            int increase = arr.get(3);

            switch (arr.get(2)) {
                case 0:
                    increase += player.getHp();

                    if(increase <= player.maxHP) { //makes it so can't heal more than max hp
                        player.setHp(increase);
                    } else {
                        player.setHp(player.maxHP);
                    }

                    hpDisplayP.setText(player.getHp() + "     ");
                    healthColorChange();
                    break;
                case 1:
                    player.setMagic(player.getMagic() + increase);
                    break;
                default:
                    break;
            }

            weaponAndPotionModel.set(potNum + 3, " "); // prevents potions for reappearing

            choiceButtons.get(potNum).setEnabled(false);
            choiceButtons.get(potNum).setText("---");
            potionsUsedInBattleList.add(curPotion);
        }


    }


    // Defend button listener
    private void defendButtonListener(JButton defendButton) {
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                combatMec('d');
                System.out.println("Defend button clicked");
            }
        };
        defendButton.addActionListener(buttonListener);
    }

    // Creates the button listeners for the talk options
    private void talkButtonListener() {
        ActionListener buttonListener = new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                int attackChance = (int)(Math.random()*100);
                if(e.getSource() == choiceButtons.get(0)){ //for blah 1
                    currentText.setText("You make small talk with the your opponent.");
                    if (tempChat >= 3) {
                        mercyKillbuttonSetup("Your opponent decides that you would be a great friend.");
                        setUpStory();
                    }
                    if (attackChance > 50) {
                        combatMec('t');
                    }
                    tempChat++;
                    System.out.println("Chat");

                } else if(e.getSource() == choiceButtons.get(1)){ // for blah 2
                    currentText.setText("You attempt to persuade your opponent to lower their weapon.");
                    if (tempPersuasion >= 3) {
                        mercyKillbuttonSetup("Your opponent succumbs to your persuasion. They are now disarmed");
                        setUpStory();
                    }
                    if (attackChance > 50) {
                        combatMec('t');
                    }
                    tempPersuasion++;
                    System.out.println("Persuade");

                } else if(e.getSource() == choiceButtons.get(2)){ // for blah 3
                    currentText.setText("Your rude remarks angers your adversary.");
                    if (tempInsult >= 3) {
                        returnButtonSetup("Your opponent explodes from anger.");
                        setUpStory();
                    }
                    if (attackChance > 50) {
                        combatMec('t');
                    }
                    tempInsult++;
                    System.out.println("Insult");

                } else if(e.getSource() == choiceButtons.get(3)){ //for back button
                    battleState = "start";
                    updateBattleState();
                }
            }
        };
        for(int i = 0; i < 4; i++) {
            choiceButtons.get(i).addActionListener(buttonListener);
        }
    }

    // Removes the button listeners for the current choiceButtons
    private void removeListeners() {
        for (int i = 0; i < 4; i++) {
            JButton button = choiceButtons.get(i);
            for (ActionListener a : button.getActionListeners()) {
                button.removeActionListener(a);
            }
        }
    }

    // Creates the stats for the enemy and player
    private void playerAndEnemySetup() {
        Random rand = new Random();
        if(enemy == null){
            enemy = new Player();
        }
        int difficultyLevel = enemy.getEnemyDifficulty();

        if(difficultyLevel == -1) {
            difficultyLevel = rand.nextInt(5 - 1) + 1;
        }

        int health = 0;
        int damageBound = 0;
        int strength = 0;
        int armor = 0;
        int magic = 0;
        int dexterity = 0;

//        int healthUpperbound = 26;
//        int healthLowerbound = 15;
//        int damageUpperbound = 11;
//        int damageLowerbound = 3;
//        int armorUpperbound = 11;
//        int armorLowerbound = 3;
//        int strengthUpperbound = 16;
//        int strengthLowerbound = 5;
//        int magicUpperbound = 16;
//        int magicLowerbound = 5;
//        int dexterityUpperbound = 16;
//        int dexterityLowerbound = 5;


        if(difficultyLevel == 1) {
            health = rand.nextInt(26 - 15) + 15;
            damageBound = rand.nextInt(11 - 3) + 3;
            armor = rand.nextInt(11 - 3) + 3;
            strength = rand.nextInt(16 - 5) + 5;
            magic = rand.nextInt(16 - 5) + 5;
            dexterity = rand.nextInt(16 - 5) + 5;


        } else if(difficultyLevel == 2) {
            health = rand.nextInt(26 - 15) + 15;
            damageBound = rand.nextInt(11 - 3) + 3;
            armor = rand.nextInt(11 - 3) + 3;
            strength = rand.nextInt(21 - 10) + 10;
            magic = rand.nextInt(21 - 10) + 10;
            dexterity = rand.nextInt(21 - 10) + 10;

        } else if(difficultyLevel == 3) {
            health = rand.nextInt(36 - 26) + 26;
            damageBound = rand.nextInt(16 - 8) + 8;
            armor = rand.nextInt(16 - 8) + 8;
            strength = rand.nextInt(21 - 10) + 10;
            magic = rand.nextInt(21 - 10) + 10;
            dexterity = rand.nextInt(21 - 10) + 10;

        } else if(difficultyLevel == 4) {
            health = rand.nextInt(36 - 26) + 26;
            damageBound = rand.nextInt(16 - 8) + 8;
            armor = rand.nextInt(21 - 8) + 8;
            strength = rand.nextInt(26 - 10) + 10;
            magic = rand.nextInt(26 - 10) + 10;
            dexterity = rand.nextInt(26 - 10) + 10;

        } else if(difficultyLevel == 5) {
            health = rand.nextInt(46 - 36) + 36;
            damageBound = rand.nextInt(19 - 10) + 10;
            armor = rand.nextInt(21 - 8) + 8;
            strength = rand.nextInt(31 - 15) + 15;
            magic = rand.nextInt(31 - 15) + 15;
            dexterity = rand.nextInt(31 - 15) + 15;

        }

        enemy.setHp(health);
        enemy.setDamageBound(damageBound);
        enemy.setArmorClass(armor);
        enemy.setStrength(strength);
        enemy.setMagic(magic);
        enemy.setDexterity(dexterity);

        System.out.println(
                "Enemy Stats\nHealth: " + health + "\n" +
                "DamageBound: " + damageBound + "\n" +
                "Armor: " + armor + "\n" +
                "Strength: " + strength + "\n" +
                "Magic: " + magic + "\n" +
                "Dexterity: " + dexterity + "\n" +
                "Difficulty Level: " + difficultyLevel
        );


        halfEnemyHP = enemy.getHp() / 2;
        halfPlayerHP = player.getHp() / 2;

        hpDisplayE.setText(enemy.getHp() + " ");
        hpDisplayP.setText(player.getHp() + " ");
        currentText.setText("You've encountered an enemy! What will you do?"); // Make a display area THEN put an HP part
        moraleDisplay.setText(player.getMorale() + " ");
    }


    // Calculates how much damage the player and/or enemy does
    private void combatMec(char attack) {
        int playerAttackValue;
        String playerAttackType;
        boolean playerDefended = false;
        switch (attack) {
            case 'p':
                playerAttackValue = player.strengthAttack();
                playerAttackType = "You swing at your enemy!";
                break;
            case 'r':
                playerAttackValue = player.dexterityAttack();
                playerAttackType = "You shoot at your enemy!";
                break;
            case 'm':
                playerAttackValue = player.magicAttack();
                playerAttackType = "You cast a spell!";
                break;
            case 'd':
                playerAttackValue = player.defend();
                playerAttackType = "You prepare for your enemy's attack.";
                playerDefended = true;
                break;
            case 't':
                playerAttackValue = 0;
                playerAttackType = "you have talked with the enemy";
                break;
            default:
                playerAttackValue = 0;
                playerAttackType = "Something is wrong";
                System.out.println("Error with combat");
                break;
        }

        // Calculates player's damage done
        int playerDamage;
        if(playerAttackValue > enemy.getArmorClass() && playerDefended == false) {
            playerDamage = player.calculateDamage();
            if (playerAttackType.equals("You swing at your enemy!")) {
                player.setMeleeHits(player.getMeleeHits() + 1);
            }
            else if (playerAttackType.equals("You shoot at your enemy!")) {
                player.setRangedHits(player.getRangedHits() + 1);
            }
            else {
                player.setMagicHits(player.getMagicHits() + 1);
            }
            currentText.setText(playerAttackType + "\n\n" + "It hit!");
        } else if(playerDefended == true) {
            playerDamage = 0;
            currentText.setText(playerAttackType);
        } else {
            playerDamage = 0;
            currentText.setText(playerAttackType + "\n\n" + "Oh no you missed!");
        }

        enemy.setHp(enemy.getHp() - playerDamage);

        if(enemy.getHp() <= 0) {
            healthColorChange();
            hpDisplayE.setText(enemy.getHp() + " ");
            hpDisplayP.setText(player.getHp() + "     ");

            removeUsedPotions();
            hpDisplayP.setText(player.getHp() + " ");
            player.setArmorClass(player.getNormalArmorClass());
            player.setMorale(50);
            restartButtonSetup(1);

        } else { //TODO: in future implement delay in display and also disable?
            // Calculates enemy's damage done

            int enemyAttackValue = findEnemyAttackType();
            int enemyDamage;

            if(enemyAttackValue > player.getArmorClass() && playerDefended == false){
                enemyDamage = enemy.calculateDamage();

                currentText.append("\n\nYour enemy's attack hits. Ouch!");

            } else if(enemyAttackValue > player.getArmorClass() && playerDefended == true){

                //TODO: Find balance for
                enemyDamage = enemy.calculateDamage();
                System.out.println("Damage before: " + enemyDamage);

                enemyDamage -= player.defendProtection();
                System.out.println("Damage after: " + enemyDamage);

                if(enemyDamage <= 0) {
                    enemyDamage = 1;
                }

                currentText.append("\n\nYour enemy's strike hits, but your defence helped!\n\nTis but a flesh wound!");

            } else {
                enemyDamage = 0;

                currentText.append("\n\nYour enemy missed!");
            }

            player.setHp(player.getHp() - enemyDamage);
            moraleChange = (playerDamage - enemyDamage) * 2;
            player.setMorale(player.getMorale() + moraleChange);
            player.setAcAffectedArmorClass();
            if (player.getAcAffectedArmorClass() == player.getNormalArmorClass()) {
                moraleDisplay.setForeground(Color.YELLOW);
            }
            else if (player.getAcAffectedArmorClass() > player.getNormalArmorClass()) {
                moraleDisplay.setForeground(Color.GREEN);
            }
            else {
                moraleDisplay.setForeground(Color.RED);
            }
            player.setArmorClass(player.getAcAffectedArmorClass());
            System.out.println(player.getArmorClass());
            moraleChange = 0;
            moraleDisplay.setText(player.getMorale() + " ");
        }

        if(player.getHp() <= 0) {
            healthColorChange();
            hpDisplayE.setText(enemy.getHp() + " ");
            hpDisplayP.setText(player.getHp() + "     ");

            removeUsedPotions();
            hpDisplayP.setText(player.getHp() + " ");
            player.setArmorClass(player.getNormalArmorClass());
            player.setMorale(50);
            restartButtonSetup(0);
        }

        healthColorChange();
        hpDisplayE.setText(enemy.getHp() + " ");
        hpDisplayP.setText(player.getHp() + " ");

    }

    private void removeUsedPotions() {
        for(int i = 0; i < potionsUsedInBattleList.size(); i++) {
            if(storage.containsKey(potionsUsedInBattleList.get(i))) {
                storage.remove(potionsUsedInBattleList.get(i));
            }
        }
    }

    // Finds which attack stat is the largest, middle, and lowest for the enemy
        // 70% to use largest stat attack
        // 20% to use middle stat attack
        // 10% to use lowest stat attack
    private int findEnemyAttackType() {
        int arr[] = {enemy.getStrength(), enemy.getDexterity(), enemy.getMagic()};
        int max = arr[0];
        int min = arr[0];
        int usedIMax = 0;
        int usedIMin = 0;
        int mid = 0;

        for(int i = 0; i < arr.length; i++) {
            if(arr[i] > max) {
                max = arr[i];
                usedIMax = i;
            }

            if(arr[i] < min) {
                min = arr[i];
                usedIMin = i;
            }
        }

        for(int i = 0; i < arr.length; i++) {
            if(i != usedIMax && i != usedIMin) {
                mid = arr[i];
            }
        }

        System.out.println(
                "Max: " + max + "\n" +
                "Mid: " + mid + "\n" +
                "Min: " + min
        );

        Random rand = new Random();
        float chance = rand.nextFloat();

        if(chance <= 0.70f) {
            return foundEnemyAttack(arr, max);
        } else if(chance <= 0.90f) {
            return foundEnemyAttack(arr, mid);
        } else {
            return foundEnemyAttack(arr, min);
        }

    }

    // returns the found enemy attack value
    private int foundEnemyAttack(int[] arr, int type) {
        if(type == arr[0]) {
            System.out.println("Enemy attack: Strength"); //TODO: TEMP FOR DEMOING
            currentText.append("\n\n\n\nYour enemy swings at you!");
            return enemy.strengthAttack();

        } else if(type == arr[1]) {
            System.out.println("Enemy attack: Dexterity");
            currentText.append("\n\n\n\nYour enemy shoots at you!");
            return enemy.dexterityAttack();

        } else if(type == arr[2]) {
            System.out.println("Enemy attack: Magic");
            currentText.append("\n\n\n\nYour enemy casts a spell at you!");
            return enemy.magicAttack();

        }

        return 0;
    }


    // Updates health display colors
    private void healthColorChange() {
        if(player.getHp() >= halfPlayerHP) {
            hpDisplayP.setForeground(Color.decode("#74D306"));
        }
        if(player.getHp() < halfPlayerHP) {
            hpDisplayP.setForeground(Color.YELLOW);
        }
        if (player.getHp() < halfPlayerHP / 2) {
            hpDisplayP.setForeground(Color.RED);
        }

        if(enemy != null) {
            if(enemy.getHp() < halfEnemyHP) {
                hpDisplayE.setForeground(Color.YELLOW);
            }
            if (enemy.getHp() < halfEnemyHP / 2) {
                hpDisplayE.setForeground(Color.RED);
            }
        }
    }

    // Sets up the restart button for the combat simulator
    private void restartButtonSetup(int wonOrLost) {
        enableChoiceButtons();

        this.center.remove(buttonPanel);
        this.center.remove(spacing3);
        statsPanel.setVisible(false);

        styleBattleButtonPanel(1000, 80);
        buttonPanel.setLayout(new GridLayout(1, 1));

        JButton restartButton = new JButton();
        buttonStyle(restartButton);

        // Checks if player won or lost
        if(wonOrLost == 0) {
            currentText.setText("Oh no! You've lost!");
            restartButton.setText("Go to last save");

            //TODO: Edit this later to actually go to last save
            restartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    storyWindow();
                }
            });
        } else if(wonOrLost == 1) {
            currentText.setText("Your enemy has collapsed! You've won!");
            // Checks if stats are to be updated and acts accordingly
            if(this.currentNode.getUpStrength() > 0 || this.currentNode.getUpMagic() > 0 || this.currentNode.getUpDexterity() > 0 || this.currentNode.getUpArmorClass() > 0) {
                currentText.append("\n\nYour stats updated!\n");
            }
            updateStats();
            int statBefore = 0;
            if(this.currentNode.getUpStrength() > 0) {
                statBefore = player.getStrength() - this.currentNode.getUpStrength() - this.player.getMeleeHits();
                currentText.append("\n\nStrength: " + statBefore + " --> " + player.getStrength());
            }
            if(this.currentNode.getUpMagic() > 0) {
                statBefore = player.getMagic() - this.currentNode.getUpMagic() - this.player.getMagicHits();
                currentText.append("\n\nMagic: " + statBefore + " --> " + player.getMagic());
            }
            if(this.currentNode.getUpDexterity() > 0) {
                statBefore = player.getDexterity() - this.currentNode.getUpDexterity() - this.player.getRangedHits();
                currentText.append("\n\nDexterity: " + statBefore + " --> " + player.getDexterity());
            }
            if(this.currentNode.getUpArmorClass() > 0) {
                statBefore = player.getArmorClass() - this.currentNode.getUpArmorClass();
                currentText.append("\n\nArmor Class: " + statBefore + " --> " + player.getArmorClass());
            }

            restartButton.setText("Continue journey");
            restartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    storyWindow();
                }
            });
        }

        // Delays being able to restart the simulator (to prevent clicking too fast for now)
        restartButton.setEnabled(false);
        int delay = 2000;
        Timer timer = new Timer(delay, ex -> {
            restartButton.setEnabled(true);
        });
        timer.setRepeats(false);
        timer.start();


        buttonPanel.add(restartButton);
        this.center.add(buttonPanel);
    }

    // Styles the buttons
    private void buttonStyle(JButton button) {

        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setFont(textFont.deriveFont(20f));

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                button.setForeground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (gameState.equals("story")) {
                    if (e.getSource() == choiceButtons.get(0)) {
                        button.setBorder(BorderFactory.createLineBorder(Color.decode("#c39838")));
                        button.setForeground(Color.decode("#c39838"));

                    } else if (e.getSource() == choiceButtons.get(1)) {
                        button.setBorder(BorderFactory.createLineBorder(Color.decode("#28c6d7")));
                        button.setForeground(Color.decode("#28c6d7"));

                    } else if (e.getSource() == choiceButtons.get(2)) {
                        button.setBorder(BorderFactory.createLineBorder(Color.decode("#9460de")));
                        button.setForeground(Color.decode("#9460de"));

                    } else if (e.getSource() == choiceButtons.get(3)) {
                        button.setBorder(BorderFactory.createLineBorder(Color.decode("#59a668")));
                        button.setForeground(Color.decode("#59a668"));

                    } else {
                        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                        button.setForeground(Color.WHITE);
                    }
                } else {
                    button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    button.setForeground(Color.WHITE);
                }
            }
        });

    }


    public void initializeOpeningSettings() {

        //this.center.setVisible(false); // Prevents error of clicking start twice (makes loop go again)
        setCenter(new JPanel()); // Makes new JFrame for the game

        this.window.setPreferredSize(new Dimension(1280, 720));
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Buttons
        for (int i = 0; i < 4; i++) {
            choiceButtons.add(new JButton("Choice " + (i + 1)));
        }
        //set button positions
        Dimension size = choiceButtons.get(0).getPreferredSize(); //w:84 h:26
        for (int i = 0; i < choiceButtons.size(); i++) {
            JButton b = choiceButtons.get(i);
            b.setBounds(0 + i * size.width, 500, size.width, size.height); //set size of button
            this.panel.add(b);
        }
        this.currentText.setText("Welcome to our game");
        this.currentText.setSize(200, 50);
        this.currentText.setBounds(540, 335, 200, 50);
        this.panel.add(currentText);
        this.panel.setLayout(null);
        this.window.add(panel);

        this.window.setSize(1280, 720);

        this.center.setVisible(true);

    }

    // Enables the choice buttons
    public void enableChoiceButtons() {
        for(int i = 0; i < 4; i++) {
            choiceButtons.get(i).setEnabled(true);
        }
    }

    // Disables the choice buttons
    public void disableChoiceButtons() {
        for(int i = 0; i < 4; i++) {
            choiceButtons.get(i).setEnabled(false);
        }
    }

    public JFrame getWindow() {
        return window;
    }

    public void setWindow(JFrame window) {
        this.window = window;
    }

    public ArrayList<JButton> getChoiceButtons() {
        return choiceButtons;
    }

    public void setChoiceButtons(ArrayList<JButton> choiceButtons) {
        this.choiceButtons = choiceButtons;
    }

    public JTextArea getCurrentText() {
        return currentText;
    }

    public void setCurrentText(JTextArea currentText) {
        this.currentText = currentText;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public void setCenter(JPanel center) {
        this.center.setVisible(false);
        this.center = center;
    }

    public int getChoiceNumber() {
        return choiceNum;
    }

    public void setChoiceNumber(int num) {
        choiceNum = num;
    }

    public void setUpStory() {
        List<StoryNode> childrenList = new ArrayList<>();
        List<StoryNode> startChoices = new ArrayList<>();

        this.currentNode = new StoryNode(null, null, "Welcome to the game demo! What will you do first?", null);

        StoryNode choice1 = new StoryNode(null, null, "You start to bust it down and are goated with the sauce.\n\nPeople glance at you.", "Dance");
        StoryNode child1 = new StoryNode(null, null, "You bust it down harder. A crowd forms around you.", "Keep dancing");
        childrenList.add(new StoryNode(null, null, "The crowd cheers! Some people throw money at you.", null, "Keep dancing"));
        childrenList.add(new StoryNode(null, null, "'Awwwwwwww' the crowd says in disappointment. The crowd disapates", null, "Stop dancing"));
        childrenList.add(new StoryNode());
        childrenList.add(new StoryNode());

        child1.setChoices(childrenList);

        childrenList = new ArrayList<>();
        childrenList.add(child1);
        childrenList.add(new StoryNode(null, null, "You stop dancing.", null, "Stop dancing"));
        childrenList.add(new StoryNode());
        childrenList.add(new StoryNode());
        choice1.setChoices(childrenList);

        startChoices.add(choice1);

        childrenList = new ArrayList<>();
        StoryNode choice2 = new StoryNode(null, null, "You sing a merry tune! People smile as they pass you", "Sing");
        ArrayList<Integer> itemStatList = new ArrayList<>();
        itemStatList.add(2);
        itemStatList.add(0); //0 = not timelimited | != 0 is what the time limit is
        itemStatList.add(0); // what type of potion | 0 = healing | 1 = magic
        itemStatList.add(3); // increase by how much
        itemStatList.add(3); // item id for equipping

        ArrayList<StoryNode> childOfChild = new ArrayList<>();
        childOfChild.add(new StoryNode(null, null, "What is this? You open the package.",  "Open", "Minor Healing", itemStatList, 2));
        childOfChild.add(new StoryNode(null, null, "You drop the package uninterested.",  "Drop"));
        childOfChild.add(new StoryNode());
        childOfChild.add(new StoryNode());

        childrenList.add(new StoryNode(null, null, "You keep singing!\n\nSomeone approaches you and hands you something.\n\nIs this a package?", childOfChild, "Keep singing"));
        childrenList.add(new StoryNode());
        childrenList.add(new StoryNode());
        childrenList.add(new StoryNode());

        choice2.setChoices(childrenList);
        startChoices.add(choice2);

        StoryNode choice3 = new StoryNode(null, null, "You jump up and it your head on a branch!\n\nOuch!", "Jump");
        child1 = new StoryNode(null, null, "You shake the tree in anger.\n\nAs you shake the tree something falls out of it.", "Shake the tree");
        childrenList = new ArrayList<>();
        childrenList.add(new StoryNode(new Dimension(2, 2), null, "You pick up the item... Is this a map?", null, "Pick what fell out of the tree"));
        childrenList.add(new StoryNode(null, null, "You ignore the item on the ground.", null, "Don't pick it up"));
        childrenList.add(new StoryNode());
        childrenList.add(new StoryNode());
        child1.setChoices(childrenList);

        childrenList = new ArrayList<>();
        childrenList.add(child1);
        childrenList.add(new StoryNode());
        childrenList.add(new StoryNode());
        childrenList.add(new StoryNode());
        choice3.setChoices(childrenList);

        startChoices.add(choice3);

        StoryNode choice4 = new StoryNode(null, null, "You yell loudly! AHHHHH!\n\nPeople jump in fear.\n\n(why are you yelling? weirdo)", "Yell");
        child1 = new StoryNode(null, null, "You yell even louder. AAAAAAAHHHHH!\n\nA guard approaches you.", "Yell louder");
        childrenList = new ArrayList<>();
        childrenList.add(new StoryNode(null, new Player(3), "You yell at the guard to fight you.\n\nYou both square up.", "Fight the guard", 3, 1, 2, 1));
        childrenList.add(new StoryNode(null, null, "You apologize for yelling.\n\nThe guard leaves.", "Apologize to the guard for yelling"));
        childrenList.add(new StoryNode(null, new Player(), "You scream in the guards ear.\n\nHe didn't like that...", "Yell at the guard", 4, 0, 1, 0));
        childrenList.add(new StoryNode());
        child1.setChoices(childrenList);

        childrenList = new ArrayList<>();
        childrenList.add(child1);
        childrenList.add(new StoryNode(null, null, "You stop yelling. People walk by cautiously.", "Stop yelling"));
        childrenList.add(new StoryNode());
        childrenList.add(new StoryNode());
        choice4.setChoices(childrenList);
        startChoices.add(choice4);

        this.currentNode.setChoices(startChoices);
    }

    //---------------TRAVERSABLE MAP WINDOW------------------------
    private void traversableMapWindow() {
        allWindowLayout();
//        gameState = "";

        //Title Panel
        if (map == null) {
            map = new Map();
        }
//        setCenter(map);
        this.center.add(map);
        center.setVisible(true);
        map.gameWindow = this;
        map.grabFocus();
        map.setGoalCoordinates(1, 1); //FIXME delete later, test only
//        window.addKeyListener(map.getKeyListener());
//        window.addKeyListener(new ArrowKeyListener2());
    }

    public void randomEncounter() {
        fightWindow();
//        map.dispose();
//        map = null;
    }

    public void setCurrentNode(StoryNode newNode) {
        this.currentNode = newNode;
        allWindowLayout();
    }

    private void mercyKillbuttonSetup(String text) {
        this.center.remove(buttonPanel);
        this.center.remove(spacing3);
        statsPanel.setVisible(false);
        currentText.setText(text);

        styleBattleButtonPanel(500, 100);
        buttonPanel.setLayout(new BorderLayout());

        JButton mercy = new JButton();
        buttonStyle(mercy);
        mercy.setText("Mercy");

        JButton murder = new JButton();
        buttonStyle(murder);
        murder.setText("Murder");

        // Delays being able to restart the simulator (to prevent clicking too fast for now)
        mercy.setEnabled(false);
        murder.setEnabled(false);
        int delay = 750;
        Timer timer = new Timer( delay, ex -> {
            mercy.setEnabled(true);
            murder.setEnabled(true);
        });
        timer.setRepeats(false);
        timer.start();

        mercy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnButtonSetup("You part ways peacefully.");
            }
        });

        murder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enemy.setHp(0);
                returnButtonSetup("D E M O N.");
            }
        });

        buttonPanel.add(mercy, BorderLayout.WEST);
        buttonPanel.add(murder, BorderLayout.EAST);
        this.center.add(buttonPanel);
    }

    private void returnButtonSetup(String text) {
        this.center.remove(buttonPanel);
        this.center.remove(spacing3);
        statsPanel.setVisible(false);
        currentText.setText(text);
        this.center.repaint();

        styleBattleButtonPanel(1000, 80);
        buttonPanel.setLayout(new GridLayout(1, 1));

        JButton returnButton = new JButton();
        buttonStyle(returnButton);
        returnButton.setText("Return");

        // Delays being able to restart the simulator (to prevent clicking too fast for now)
        returnButton.setEnabled(false);
        int delay = 1000;
        Timer timer = new Timer( delay, ex -> {
            returnButton.setEnabled(true);
        });
        timer.setRepeats(false);
        timer.start();

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                battleState = "start";
                updateBattleState();
                storyWindow();
            }
        });

        buttonPanel.add(returnButton);
        this.center.add(buttonPanel);
    }
}