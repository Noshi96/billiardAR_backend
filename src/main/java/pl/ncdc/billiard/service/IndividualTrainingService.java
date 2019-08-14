package pl.ncdc.billiard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import pl.ncdc.billiard.commands.IndividualTrainingCommand;
import pl.ncdc.billiard.models.IndividualTraining;
import pl.ncdc.billiard.repository.IndividualTrainingRepository;

@Service
public class IndividualTrainingService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    private IndividualTrainingRepository individualTrainingRepository;
      
	
    /**
     * 
     * @return
     */
    public List<IndividualTraining> getIndividualTraining(){
    	
      List<IndividualTraining> trainingList = new ArrayList<>();
      String sql = "SELECT * FROM individual_training";
      Collection<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

      rows.stream().map((row)->{
    	  IndividualTraining individualTraining = new IndividualTraining();
    	  individualTraining.setId((Integer) row.get("id"));
    	  individualTraining.setDifficultyLvl((String) row.get("difficulty_lvl"));
    	  individualTraining.setPositionOfRectangle((String) row.get("position_of_rectangle"));
    	  individualTraining.setPositionOfSelectedBall((String) row.get("position_of_selected_ball"));
    	  individualTraining.setPositionOfWhiteBall((String) row.get("position_of_white_ball"));
    	  individualTraining.setPositionsOfDisturbBalls((String) row.get("positions_of_disturb_balls"));
    	  return individualTraining;
      }).forEach((individualTraining)->{
    	  trainingList.add(individualTraining);
      });
      System.out.print(rows);
      return trainingList;
  }
    
    /**
     * 
     * @param id
     * @return
     */
    public List<IndividualTraining> getIndividualTrainingById(int id){
        List<IndividualTraining> trainingList = new ArrayList<>();
        String sql = "SELECT * FROM individual_training WHERE id='"+ id +"'";
        Collection<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        rows.stream().map((row)->{
      	  IndividualTraining individualTraining = new IndividualTraining();
      	  individualTraining.setId((Integer) row.get("id"));
      	  individualTraining.setDifficultyLvl((String) row.get("difficulty_lvl"));
      	  individualTraining.setPositionOfRectangle((String) row.get("position_of_rectangle"));
      	  individualTraining.setPositionOfSelectedBall((String) row.get("position_of_selected_ball"));
      	  individualTraining.setPositionOfWhiteBall((String) row.get("position_of_white_ball"));
      	  individualTraining.setPositionsOfDisturbBalls((String) row.get("positions_of_disturb_balls"));
      	  return individualTraining;
        }).forEach((individualTraining)->{
      	  trainingList.add(individualTraining);
        });
        System.out.print(rows);
        return trainingList;
    }
    
    /**
     * 
     * @param lvl
     * @return
     */
    public List<IndividualTraining> getIndividualTrainingByLvl(String lvl){
        List<IndividualTraining> trainingList = new ArrayList<>();
        String sql = "SELECT * FROM individual_training WHERE difficulty_lvl='"+ lvl +"'";
        Collection<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        rows.stream().map((row)->{
      	  IndividualTraining individualTraining = new IndividualTraining();
      	  individualTraining.setId((Integer) row.get("id"));
      	  individualTraining.setDifficultyLvl((String) row.get("difficulty_lvl"));
      	  individualTraining.setPositionOfRectangle((String) row.get("position_of_rectangle"));
      	  individualTraining.setPositionOfSelectedBall((String) row.get("position_of_selected_ball"));
      	  individualTraining.setPositionOfWhiteBall((String) row.get("position_of_white_ball"));
      	  individualTraining.setPositionsOfDisturbBalls((String) row.get("positions_of_disturb_balls"));
      	  return individualTraining;
        }).forEach((individualTraining)->{
      	  trainingList.add(individualTraining);
        });
        System.out.print(rows);
        return trainingList;
    }
    
    /**
     * 
     * @param individualTraining
     */
    public void createIndividualTraining(IndividualTraining individualTraining) {
        jdbcTemplate.update((Connection connection)->{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO individual_training (difficulty_lvl,position_of_rectangle,position_of_selected_ball,position_of_white_ball,positions_of_disturb_balls) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, individualTraining.getDifficultyLvl());
            preparedStatement.setString(2, individualTraining.getPositionOfRectangle());
            preparedStatement.setString(3, individualTraining.getPositionOfSelectedBall());
            preparedStatement.setString(4, individualTraining.getPositionOfWhiteBall());
            preparedStatement.setString(5, individualTraining.getPositionsOfDisturbBalls());
            return preparedStatement;
        });
    }
    
    /**
     * 
     * @param individualTraining
     * @param id
     */
    public void updateIndividualTraining(IndividualTraining individualTraining, int id) {
        jdbcTemplate.update((Connection connection)->{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE individual_training SET difficulty_lvl=?, position_of_rectangle=?, position_of_selected_ball=?, position_of_white_ball=?, positions_of_disturb_balls=?  WHERE id="+ id +"");
            preparedStatement.setString(1, individualTraining.getDifficultyLvl());
            preparedStatement.setString(2, individualTraining.getPositionOfRectangle());
            preparedStatement.setString(3, individualTraining.getPositionOfSelectedBall());
            preparedStatement.setString(4, individualTraining.getPositionOfWhiteBall());
            preparedStatement.setString(5, individualTraining.getPositionsOfDisturbBalls());
            return preparedStatement;
        });
    }
    
	public IndividualTraining fetch(Long id) {
		Optional<IndividualTraining> optionalIndividualTraining = individualTrainingRepository.findById(id);
		if(!optionalIndividualTraining.isPresent()) {
			return null;
		}
		return optionalIndividualTraining.get();
	}
	
	
	/**
	 * W bazie nie mo¿e byæ nullów
	 * @param id
	 * @return
	 */
	 public List<IndividualTrainingCommand> returnIndividualTrainingCommand(long id){
		 
		 List<IndividualTrainingCommand> individualTrainingCommandList = new ArrayList<>();
		 IndividualTrainingCommand individualTrainingCommand = new IndividualTrainingCommand();
		 
		 IndividualTraining individualTraining = fetch(id);
		 
		 String[] posWhiteBallAndSelected =  individualTraining.getPositionOfWhiteBall().split(",");
		 String[] posDisturbBalls =  individualTraining.getPositionsOfDisturbBalls().split(",");
		 String[] posRectangle =  individualTraining.getPositionOfRectangle().split(",");
		 
		 if (posWhiteBallAndSelected.length != 0 ) {
			 individualTrainingCommand.setId(individualTraining.getId());
			 individualTrainingCommand.setDifficultyLvl(individualTraining.getDifficultyLvl());
			 individualTrainingCommand.setPositionOfWhiteBallX(posWhiteBallAndSelected[0]);
			 individualTrainingCommand.setPositionOfWhiteBallY(posWhiteBallAndSelected[1]);
			 posWhiteBallAndSelected = individualTraining.getPositionOfSelectedBall().split(",");
			 if (posWhiteBallAndSelected.length != 0 ) {
				 individualTrainingCommand.setPositionOfSelectedBallX(posWhiteBallAndSelected[0]);
				 individualTrainingCommand.setPositionOfSelectedBallY(posWhiteBallAndSelected[1]);	
				 }
		 	}
		 	 
		 individualTrainingCommandList.add(individualTrainingCommand);
		 
		 if (posDisturbBalls.length != 0 && posDisturbBalls.length % 2 == 0 )  {			 
			 for(int i = 0; i < posDisturbBalls.length; i=i+2) {
				 
				 IndividualTrainingCommand individualTrainingCommand2 = new IndividualTrainingCommand();
				 individualTrainingCommand2.setPositionsOfDisturbBallsX(posDisturbBalls[i]);
				 individualTrainingCommand2.setPositionsOfDisturbBallsY(posDisturbBalls[i+1]);
				 individualTrainingCommandList.add(individualTrainingCommand2);
			 	}
		 }
		 if (posRectangle.length != 0 && posRectangle.length % 2 == 0) {
			 for(int i = 0; i < posRectangle.length; i=i+2) {
				 
				 IndividualTrainingCommand individualTrainingCommand2 = new IndividualTrainingCommand();
				 individualTrainingCommand2.setPositionOfRectangleX(posRectangle[i]);
				 individualTrainingCommand2.setPositionOfRectangleY(posRectangle[i+1]);
				 individualTrainingCommandList.add(individualTrainingCommand2);
			 	} 
		 }
		 return individualTrainingCommandList;
	 }
	
    
    
    public List<IndividualTrainingCommand> returnPoints(int id) {
        List<IndividualTraining> trainingList = new ArrayList<>();
        List<String> listPointsX = new ArrayList<>();
        List<String> listPointsY = new ArrayList<>();
        List<String> listOfDisturbBalls = new ArrayList<>();
        List<String> positionOfRectangle = new ArrayList<>();
        String sql = "SELECT * FROM individual_training WHERE id='"+ id +"'";
        Collection<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        
        
        List<IndividualTrainingCommand> individualTrainingCommandList = new ArrayList<>();
        
        rows.stream().map((row)->{
        	  IndividualTraining individualTraining = new IndividualTraining();
        	  individualTraining.setId((Integer) row.get("id"));
        	  individualTraining.setDifficultyLvl((String) row.get("difficulty_lvl"));
        	  individualTraining.setPositionOfRectangle((String) row.get("position_of_rectangle"));
        	  individualTraining.setPositionOfSelectedBall((String) row.get("position_of_selected_ball"));
        	  individualTraining.setPositionOfWhiteBall((String) row.get("position_of_white_ball"));
        	  individualTraining.setPositionsOfDisturbBalls((String) row.get("positions_of_disturb_balls"));
        	  return individualTraining;
          }).forEach((individualTraining)->{
        	  listOfDisturbBalls.add(individualTraining.getPositionsOfDisturbBalls());
          });
//        String[] points = listOfDisturbBalls.get(0).split(",");
//        for(int i = 0; i < points.length; i=i+2 ) {
//        	IndividualTrainingCommand individualTrainingCommand = new IndividualTrainingCommand();
//        	individualTrainingCommand.setPositionsOfDisturbBallsX(points[i]);
//        	individualTrainingCommand.setPositionsOfDisturbBallsY(points[i+1]);
//        	individualTrainingCommandList.add(individualTrainingCommand);
//        }
       

          //System.out.print(points.length);
          return individualTrainingCommandList;
        
    }
    
}
