package facespace;

import java.util.ArrayList;

class UserInfo {

	public String username;
	public String description;
	public boolean isPolyamorous;
	public String friends;
	public String partners;
	public ArrayList<String> friendArrayList;
	public ArrayList<String> partnerArrayList;
	public int reference;
	
	public UserInfo(String username, String description, boolean isPolyamorous, String friends, String partners, ArrayList<String> friendArrayList, ArrayList<String> partnerArrayList, int reference){
		this.username = username;
		this.description = description;
		this.isPolyamorous = isPolyamorous;
		this.friends = friends;
		this.partners = partners;
		this.friendArrayList = friendArrayList;
		this.partnerArrayList = partnerArrayList;
		this.reference = reference;
	}
}
