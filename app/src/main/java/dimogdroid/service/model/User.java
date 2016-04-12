/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dimogdroid.service.model;

/**
 *
 * @author ffernandez
 */
public class User {
    
    private String username;
    private int imgicon;
    private int position;
    private int time;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getImgicon() {
        return imgicon;
    }

    public void setImgicon(int imgicon) {
        this.imgicon = imgicon;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

   
}
