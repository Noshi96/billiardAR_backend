package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.opencv.core.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.commands.IndividualTrainingCommand;
import pl.ncdc.billiard.models.BilliardTable;
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
     * @return Zwraca wszystkie rekordy z bazy
     */
	public List<IndividualTraining> fetchAll(){
		return individualTrainingRepository.findAll();
	}
	
	/**
	 * Metoda zwraca liste posortowana po lvlach
	 * @param lvl Poziom trudnosci okreslony w parametrze
	 * @return Lista posortowana po lvlach
	 */
	public List<IndividualTraining> sortListByLvl(String lvl){
		List<IndividualTraining> list = fetchAll();
		List<IndividualTraining> listSortedByLvl = new ArrayList<>();
		for(IndividualTraining element : list) {
			if (element.getDifficultyLvl().equals(lvl)) {
				listSortedByLvl.add(element);
			} 	
		}
		return listSortedByLvl;
	}
   
  
    /**
     * Metoda aktualizuje rekord o podanym Id
     * @param individualTrainingCommand
     * @param id Rekordu w bazie
     * @return
     */
	public IndividualTraining updateIndividualTraining(IndividualTrainingCommand individualTrainingCommand, Long id) {
		
		IndividualTraining individualTraining = fetch(id);
		
		if (individualTraining != null) {
			individualTraining.setDifficultyLvl(individualTrainingCommand.getDifficultyLvl());
			individualTraining.setPositionOfRectangle(individualTrainingCommand.getPositionOfRectangle());
			individualTraining.setPositionOfSelectedBall(individualTrainingCommand.getPositionOfSelectedBall());
			individualTraining.setPositionOfWhiteBall(individualTrainingCommand.getPositionOfWhiteBall());
			individualTraining.setPositionsOfDisturbBalls(individualTrainingCommand.getPositionsOfDisturbBalls());
		}
		
		return individualTrainingRepository.save(individualTraining);
	}
     
    /**
     * 
     * @param id Id IndividualTrainingu
     * @return Zwraca obiekt klasy IndividualTraining z bazy o podanym w parametrze id
     */
	public IndividualTraining fetch(Long id) {
		Optional<IndividualTraining> optionalIndividualTraining = individualTrainingRepository.findById(id);
		if(!optionalIndividualTraining.isPresent()) {
			return null;
		}
		return optionalIndividualTraining.get();
	}
	
	/**
	 * Zwraca wszystkie informacje na temat treningu w postaci listy rekordow, gdzie w pierwszym obiekcie mamy
	 * informacje o polozeniu bil bialej oraz zaznaczonej i okreslony poziom trudnosci.
	 * Kolejne obiekty sa reprezentacja pojedynczych bil przeszkadzajacych lub wspolzednych dla rysowanego prostokata
	 * @param id Treningu indywidualnego
	 * @return
	 */
	// W bazie nie moze byc nullow
	 public List<IndividualTrainingCommand> returnIndividualTrainingCommand(long id){
		 
		 List<IndividualTrainingCommand> individualTrainingCommandList = new ArrayList<>();
		 IndividualTrainingCommand individualTrainingCommand = new IndividualTrainingCommand();
		 
		 IndividualTraining individualTraining = fetch(id);
		 long sameId = individualTraining.getId();
		 
		 String[] posWhiteBallAndSelected =  individualTraining.getPositionOfWhiteBall().split(",");
		 String[] posDisturbBalls =  individualTraining.getPositionsOfDisturbBalls().split(",");
		 String[] posRectangle =  individualTraining.getPositionOfRectangle().split(",");
		 
		 if (posWhiteBallAndSelected.length != 0 ) {
			 individualTrainingCommand.setId(individualTraining.getId());
			 individualTrainingCommand.setDifficultyLvl(individualTraining.getDifficultyLvl());
			 individualTrainingCommand.setPositionOfWhiteBallX(posWhiteBallAndSelected[0]);
			 individualTrainingCommand.setPositionOfWhiteBallY(posWhiteBallAndSelected[1]);
			 posWhiteBallAndSelected = individualTraining.getPositionOfSelectedBall().split(",");
			 
				 individualTrainingCommand.setPositionOfSelectedBallX(posWhiteBallAndSelected[0]);
				 individualTrainingCommand.setPositionOfSelectedBallY(posWhiteBallAndSelected[1]);	
		 	}
		 	 
		 individualTrainingCommandList.add(individualTrainingCommand);
		 
		 if (posDisturbBalls.length != 0 && posDisturbBalls.length % 2 == 0 )  {			 
			 for(int i = 0; i < posDisturbBalls.length; i=i+2) {
				 IndividualTrainingCommand individualTrainingCommand2 = new IndividualTrainingCommand();
				 individualTrainingCommand2.setId(sameId);
				 individualTrainingCommand2.setPositionsOfDisturbBallsX(posDisturbBalls[i]);
				 individualTrainingCommand2.setPositionsOfDisturbBallsY(posDisturbBalls[i+1]);
				 individualTrainingCommandList.add(individualTrainingCommand2);
			 	}
		 }
		 if (posRectangle.length != 0 && posRectangle.length % 2 == 0) {
			 for(int i = 0; i < posRectangle.length; i=i+2) {
				 IndividualTrainingCommand individualTrainingCommand2 = new IndividualTrainingCommand();
				 individualTrainingCommand2.setId(sameId);
				 individualTrainingCommand2.setPositionOfRectangleX(posRectangle[i]);
				 individualTrainingCommand2.setPositionOfRectangleY(posRectangle[i+1]);
				 individualTrainingCommandList.add(individualTrainingCommand2);
			 	} 
		 }
		 return individualTrainingCommandList;
	 }
	 	 
	 /**
	  * Zapisuje nowy rekord do bazy
	  * W JSONIE kolejne wspolzedne sa podawane jako X,Y,X,Y kazda kolejna para to pojedyncza bila
	  * @param individualTrainingCommand
	  * @return
	  */
		public IndividualTraining save(IndividualTrainingCommand individualTrainingCommand) {
			IndividualTraining individualTraining = null;
			
			if(individualTrainingCommand.getId() != null) {
				Optional<IndividualTraining> optionalIndividualTraining = individualTrainingRepository.findById(individualTrainingCommand.getId());			
				if(optionalIndividualTraining.isPresent()) {
					individualTraining = optionalIndividualTraining.get();	
					
					individualTraining.setDifficultyLvl(individualTrainingCommand.getDifficultyLvl());
					individualTraining.setPositionOfRectangle(individualTrainingCommand.getPositionOfRectangle());
					individualTraining.setPositionOfSelectedBall(individualTrainingCommand.getPositionOfSelectedBall());
					individualTraining.setPositionOfWhiteBall(individualTrainingCommand.getPositionOfWhiteBall());
					individualTraining.setPositionsOfDisturbBalls(individualTrainingCommand.getPositionsOfDisturbBalls());
					individualTraining.setIdPocket(individualTrainingCommand.getIdPocket());
				} else {
					return null;
				}
			} else {		
				individualTraining = new IndividualTraining(individualTrainingCommand.getDifficultyLvl(), individualTrainingCommand.getPositionOfWhiteBall(), individualTrainingCommand.getPositionOfSelectedBall(), individualTrainingCommand.getPositionsOfDisturbBalls(), individualTrainingCommand.getPositionOfRectangle(), individualTrainingCommand.getIdPocket());
			}
			return individualTrainingRepository.save(individualTraining);
		}
				
		/**
		 * Usuwa Indywidualny trening na podstawie Id podanego w parametrze
		 * @param id
		 * @return
		 */
		public IndividualTraining delete(Long id) {
			IndividualTraining individualTraining = fetch(id);
			if(individualTraining == null) {
				return null;
			} else {				
				individualTrainingRepository.delete(individualTraining);
				return individualTraining;
			}
		}
		
		
		/**
		 * !!! nie powinno byc += w 214?, 198;;
		 * @param billiardTable
		 * @return Pobiera aktualne dane ze stolu i wpisuje do obiektu treningu
		 */
	    public IndividualTraining setNewTraining(BilliardTable billiardTable) {
	    	
	    	int idPocket = billiardTable.getSelectedPocket().getId();
	    	String positionOfWhiteBall = Double.valueOf(billiardTable.getWhiteBall().getPoint().x).toString() + "," +
	    			Double.valueOf(billiardTable.getWhiteBall().getPoint().y).toString();;
	    	String positionOfSelectedBall = Double.valueOf(billiardTable.getSelectedBall().getPoint().x).toString() + "," +
	    			Double.valueOf(billiardTable.getSelectedBall().getPoint().y).toString();;
	    	
	    	String difficultyLevel = billiardTable.getDifficultyLevel();
	    	
	    	String positionOfRectangle = Double.valueOf(billiardTable.getYellowBox().get(0).x).toString() + "," +
	    			Double.valueOf(billiardTable.getYellowBox().get(0).y).toString() + "," +
	    			Double.valueOf(billiardTable.getYellowBox().get(1).x).toString() + "," +
	    			Double.valueOf(billiardTable.getYellowBox().get(1).y).toString();
	    	
	    	
	    	List<Point> disturbPoints = billiardTable.getDisturbPoints();
	    	String positionOfDisturbBalls = "";
	    	
	    	for (int x = 0; x < disturbPoints.size(); x++) {
	    		positionOfDisturbBalls = Double.valueOf(disturbPoints.get(x).x).toString() + Double.valueOf(disturbPoints.get(x).y).toString();
	    	}
	    	
	    	IndividualTraining individualTraining = new IndividualTraining(difficultyLevel, positionOfWhiteBall, positionOfSelectedBall, positionOfDisturbBalls, positionOfRectangle, idPocket);
	    	
	    	return individualTrainingRepository.save(individualTraining);
	    }

	   
}
