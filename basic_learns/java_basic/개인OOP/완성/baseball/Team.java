package baseball;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Team implements Serializable{
	public static final String[] defaultTeam = { "�λ�", "NC", "�ؼ�", "��ȭ" };
	
	private String name;
	private Set<Player> playerSet;
	
	public Team(String name){
		this.name = name;
		playerSet = new HashSet<>();
	}	
	public String getName(){
		return name;		
	}	
	public Set<Player> getPlayersSet(){
		return playerSet;
	}
	public boolean existPlayer(String name){		
		return playerSet.contains(new Player(name));
	}	
}
