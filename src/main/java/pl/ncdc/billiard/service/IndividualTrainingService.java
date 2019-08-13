package pl.ncdc.billiard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.commands.IndividualTrainingCommand;
import pl.ncdc.billiard.entity.IndividualTraining;

@Service
public class IndividualTrainingService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
      
	
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
    
    public List<IndividualTraining> returnPoints(int id) {
        List<IndividualTraining> trainingList = new ArrayList<>();
        List<String> listPointsX = new ArrayList<>();
        List<String> listPointsY = new ArrayList<>();
        List<String> listPoints = new ArrayList<>();
        String sql = "SELECT * FROM individual_training WHERE id='"+ id +"'";
        Collection<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        
        IndividualTrainingCommand individualTrainingCommand = new IndividualTrainingCommand();
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
        	  listPoints.add(individualTraining.getPositionsOfDisturbBalls());
          });
        for(int i = 0; i < listPoints.size(); i++ ) {
        	if (i % 2 == 0) {
        		listPointsX.add((listPoints.get(i).split(","))[i]);
        		individualTrainingCommand.setPositionsOfDisturbBallsX(listPointsX.get(i));
        		individualTrainingCommandList.add(individualTrainingCommand);
        	} else {
        		listPointsY.add((listPoints.get(i).split(","))[i]);
        		individualTrainingCommand.setPositionsOfDisturbBallsY(listPointsX.get(i));
        		individualTrainingCommandList.add(individualTrainingCommand);
        	}
        }
        	
          //System.out.print(rows);
          return trainingList;
        
    }
    
}
