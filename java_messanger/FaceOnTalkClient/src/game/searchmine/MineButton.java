package game.searchmine;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MineButton extends JButton{	
	//static fields
	private static ImageIcon flagImage = new ImageIcon("image/mine/flag.jpg");
	private static ImageIcon mineImage = new ImageIcon("image/mine/mine.jpg");	
	private static ImageIcon wrongCheckImage = new ImageIcon("image/mine/wrongCheck.jpg");
	private static ImageIcon[] numberImages = new ImageIcon[9];
	static{
		for(int i=0;i<numberImages.length;i++){
			numberImages[i] = new ImageIcon("image/mine/no"+i+".jpg"); 
		}		
	}	
	private int row,col;
	private boolean isSelected = false;
	private boolean isCheckedMine = false;
	private int number;
	
	public MineButton(int number,int y,int x) {		
		row = y;
		col = x;
		this.number = number;		
	}	
	public int getRow(){
		return row;
	}	
	public int getCol(){
		return col;
	}	
	public int changeFlagImage(boolean isMine) {		
		int result=0;
		if(isCheckedMine) { // üũ Ǯ��
			this.setIcon(null);			
			isCheckedMine = false;
			if(isMine) //�����ε� üũ Ǯ��
				result = 1;			
			else //���ξƴѵ� üũǮ��
				result = -1;
		} else { //üũ��
			setIcon(flagImage);
			isCheckedMine = true;
			if(isMine)				
				result = -1; //üũ ����� ��
			else
				result = 1;
		}	
		return result;
	}		
	public void showNumberImage() {
		if(!isSelected){
			isSelected = true;
			setIcon(numberImages[number]);			
		}		
	}	
	public void showMineImage() {
		setIcon(mineImage);		
	}	
	public void showWrongCheck() {
		setIcon(null);
		setIcon(wrongCheckImage);
	}
	public boolean isChecked() { //��߷� üũ
		return isCheckedMine;
	}
	public boolean isClicked() { //Ŭ��
		return isSelected;
	}
}