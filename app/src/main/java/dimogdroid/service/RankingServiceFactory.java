/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dimogdroid.service;

import retrofit.RestAdapter;

/**
 *
 * @author ffernandez
 */
public class RankingServiceFactory {
    
    public static RankingService createRankingService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://dimogaru.hol.es/Comparteme/v1")
            .build();
        RankingService service = restAdapter.create(RankingService.class);
        
        return service;
//        return new MockRankingService();
    }
    
//    static class MockRankingService implements RankingService {
//
////        public Result listRanking(int level, String deviceId) {
////            Result result = new Result();
////            
////            // Mi Usuario
////            User user = new User();
////            user.setUsername("Mi Usuario");
////            user.setPosition(13);
////            user.setTime(3500);
////            
////            result.setUser(user);
////            
////            //Ranking
////            Ranking ranking = new Ranking();
////            List<User> lstUser = new ArrayList<User>();
////            
////            long x = 1234567L;
////            long y = 23456789L;
////            for (int i = 1; i < 11; i++) {
////            	user = new User();
////                user.setUsername("Usuario" + i);
////                user.setPosition(i);
////                
////                Random r = new Random();
////                int number = (int) (x+((int)(r.nextDouble()*(y-x))));
////                user.setTime(number);
////                
////                lstUser.add(user);
////			}
////            ranking.setUserList(lstUser);
////            result.setRanking(ranking);
////            
////            
////            //UserRanking
////            //aboveList
////            
////            UserRanking userRanking = new UserRanking();
////            List<User> lstaboveList = new ArrayList<User>();
////            
////            for (int i = 11; i < 13; i++) {
////            	user = new User();
////                user.setUsername("Usuario Superior" + i);
////                user.setPosition(i);
////                
////                Random r = new Random();
////                int number = (int) (x+((int)(r.nextDouble()*(y-x))));
////                user.setTime(number);
////                
////                lstaboveList.add(user);
////			}
////            
////            
////          //aboveList
////            List<User> lstbelowList = new ArrayList<User>();
////            
////            for (int i = 14; i < 16; i++) {
////            	user = new User();
////                user.setUsername("Usuario Inferior" + i);
////                user.setPosition(i);
////                
////                Random r = new Random();
////                int number = (int) (x+((int)(r.nextDouble()*(y-x))));
////                user.setTime(number);
////                
////                lstbelowList.add(user);
////			}
////            
////            userRanking.setAboveList(lstaboveList);
////            userRanking.setBelowList(lstbelowList);
////            
////            
////            result.setUserRanking(userRanking);
////
////            return result;
////        }
//
//    	
//		@Override
//		public User postTime(int level, String deviceId, int time,
//				String username) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//@Override
//public Result listRanking(int level, String deviceId) {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//		
//
//		
//		
//		
//		
//
////        public User postTime(int level, long time, String deviceId, String username) {
////            User user = new User();
////            user.setUsername(username);
////            user.setPosition(10);
////            user.setTime(3500);
////            return user;
////        }
//
//    }
    
}
