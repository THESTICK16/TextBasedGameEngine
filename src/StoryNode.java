import java.util.*;
import java.awt.Dimension;

public class StoryNode {

    private Dimension mapGoal;

    private Player enemyEncounter;

    private String storyText;

    private List<StoryNode> choices;

    private String buttonText;

    private Map lastMap;

    private int upStrength, upMagic, upDexterity, upArmorClass;

    private String itemString;
    private ArrayList<Integer> itemArr;

    private int itemType;

    public StoryNode(){
        this.mapGoal = null;
        this.enemyEncounter = null;
        this.storyText = null;
        this.choices = null;
        this.buttonText = null;

        this.upStrength = 0;
        this.upMagic = 0;
        this.upDexterity = 0;
        this.upArmorClass = 0;

        this.itemString = null;
        this.itemArr = null;
        this.itemType = -1;
    }

    public StoryNode(Dimension mapGoal, Player enemyEncounter, String storyText, List<StoryNode> choices, String buttonText){
        this.mapGoal = mapGoal;
        this.enemyEncounter = enemyEncounter;
        this.storyText = storyText;
        this.choices = choices;
        this.buttonText = buttonText;

        this.upStrength = 0;
        this.upMagic = 0;
        this.upDexterity = 0;
        this.upArmorClass = 0;
    }

    public StoryNode(Dimension mapGoal, Player enemyEncounter, String storyText, String buttonText){
        this.mapGoal = mapGoal;
        this.enemyEncounter = enemyEncounter;
        this.storyText = storyText;
        this.choices = null;
        this.buttonText = buttonText;

        this.upStrength = 0;
        this.upMagic = 0;
        this.upDexterity = 0;
        this.upArmorClass = 0;
    }

    public StoryNode(Dimension mapGoal, Player enemyEncounter, String storyText, List<StoryNode> choices, String buttonText, int upStrength, int upMagic, int upDexterity, int upArmorClass){
        this.mapGoal = mapGoal;
        this.enemyEncounter = enemyEncounter;
        this.storyText = storyText;
        this.choices = choices;
        this.buttonText = buttonText;

        this.upStrength = upStrength;
        this.upMagic = upMagic;
        this.upDexterity = upDexterity;
        this.upArmorClass = upArmorClass;
    }

    public StoryNode(Dimension mapGoal, Player enemyEncounter, String storyText, String buttonText, int upStrength, int upMagic, int upDexterity, int upArmorClass){
        this.mapGoal = mapGoal;
        this.enemyEncounter = enemyEncounter;
        this.storyText = storyText;
        this.buttonText = buttonText;

        this.upStrength = upStrength;
        this.upMagic = upMagic;
        this.upDexterity = upDexterity;
        this.upArmorClass = upArmorClass;
    }

    //--- with adding items

    public StoryNode(Dimension mapGoal, Player enemyEncounter, String storyText, String buttonText, int upStrength, int upMagic, int upDexterity, int upArmorClass, String itemString, ArrayList<Integer> itemArr, int itemType){
        this.mapGoal = mapGoal;
        this.enemyEncounter = enemyEncounter;
        this.storyText = storyText;
        this.buttonText = buttonText;

        this.upStrength = upStrength;
        this.upMagic = upMagic;
        this.upDexterity = upDexterity;
        this.upArmorClass = upArmorClass;

        this.itemString = itemString;
        this.itemArr = itemArr;
        this.itemType = itemType;
    }

    public StoryNode(Dimension mapGoal, Player enemyEncounter, String storyText, List<StoryNode> choices, String buttonText, int upStrength, int upMagic, int upDexterity, int upArmorClass, String itemString, ArrayList<Integer> itemArr, int itemType){
        this.mapGoal = mapGoal;
        this.enemyEncounter = enemyEncounter;
        this.storyText = storyText;
        this.choices = choices;
        this.buttonText = buttonText;

        this.upStrength = upStrength;
        this.upMagic = upMagic;
        this.upDexterity = upDexterity;
        this.upArmorClass = upArmorClass;

        this.itemString = itemString;
        this.itemArr = itemArr;
        this.itemType = itemType;
    }

    public StoryNode(Dimension mapGoal, Player enemyEncounter, String storyText, String buttonText, String itemString, ArrayList<Integer> itemArr, int itemType){
        this.mapGoal = mapGoal;
        this.enemyEncounter = enemyEncounter;
        this.storyText = storyText;
        this.choices = null;
        this.buttonText = buttonText;

        this.upStrength = 0;
        this.upMagic = 0;
        this.upDexterity = 0;
        this.upArmorClass = 0;

        this.itemString = itemString;
        this.itemArr = itemArr;
        this.itemType = itemType;
    }

    public StoryNode(Dimension mapGoal, Player enemyEncounter, String storyText, List<StoryNode> choices, String buttonText, String itemString, ArrayList<Integer> itemArr, int itemType){
        this.mapGoal = mapGoal;
        this.enemyEncounter = enemyEncounter;
        this.storyText = storyText;
        this.choices = choices;
        this.buttonText = buttonText;

        this.upStrength = 0;
        this.upMagic = 0;
        this.upDexterity = 0;
        this.upArmorClass = 0;

        this.itemString = itemString;
        this.itemArr = itemArr;
        this.itemType = itemType;
    }


    public Dimension getMapGoal() {
        return mapGoal;
    }

    public void setMapGoal(Dimension mapGoal) {
        this.mapGoal = mapGoal;
    }

    public Player getEnemyEncounter() {
        return enemyEncounter;
    }

    public void setEnemyEncounter(Player enemyEncounter) {
        this.enemyEncounter = enemyEncounter;
    }

    public String getStoryText() {
        return storyText;
    }

    public void setStoryText(String storyText) {
        this.storyText = storyText;
    }

    public List<StoryNode> getChoices() {
        return choices;
    }

    public void setChoices(List choices) {
        this.choices = choices;
    }


    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public void setUpStrength(int upStrength) {
        this.upStrength = upStrength;
    }

    public int getUpStrength() {
        return this.upStrength;
    }

    public void setUpMagic(int upMagic) {
        this.upMagic = upMagic;
    }

    public int getUpMagic() {
        return this.upMagic;
    }

    public void setUpDexterity(int upDexterity) {
        this.upDexterity = upDexterity;
    }

    public int getUpDexterity() {
        return this.upDexterity;
    }

    public void setUpArmorClass(int upArmorClass) {
        this.upArmorClass = upArmorClass;
    }

    public int getUpArmorClass() {
        return this.upArmorClass;
    }

    public void setItemString(String itemString) { this.itemString = itemString; }

    public String getItemString() { return this.itemString; }

    public void setItemArr(ArrayList<Integer> itemArr) { this.itemArr = itemArr; }

    public ArrayList<Integer> getItemArr() { return itemArr; }

    public void setItemType(int itemType) { this.itemType = itemType; }

    public int getItemType() { return itemType; }


}