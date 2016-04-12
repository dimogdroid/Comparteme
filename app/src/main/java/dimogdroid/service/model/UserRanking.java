/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dimogdroid.service.model;

import java.util.List;
 
/**
 *
 * @author ffernandez
 */
public class UserRanking {
    
    private List<User> aboveList;
    private List<User> belowList;

    public List<User> getAboveList() {
        return aboveList;
    }

    public void setAboveList(List<User> aboveList) {
        this.aboveList = aboveList;
    }

    public List<User> getBelowList() {
        return belowList;
    }

    public void setBelowList(List<User> belowList) {
        this.belowList = belowList;
    }
    
}
