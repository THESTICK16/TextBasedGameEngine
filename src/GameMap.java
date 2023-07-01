
public class GameMap {
    private char[][] map;

    public GameMap(){
        map = new char[3][3];
    }

    public GameMap(char[][] newMap){
        map = newMap;
    }

    public void setMap(char [][] newMap){
        map = newMap;
    }

    public char[][] getMap(){
        return map;
    }

}
