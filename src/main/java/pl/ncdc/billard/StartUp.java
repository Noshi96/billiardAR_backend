package pl.ncdc.billard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.ncdc.billard.repository.BallRepository;
import pl.ncdc.billard.repository.HitRepository;
import pl.ncdc.billard.repository.PocketRepository;
import pl.ncdc.billard.service.BilliardTableService;

// uzupelnic do testow
@Component
public class StartUp {
	
	@Autowired
	private BallRepository ballRepository;
	
	@Autowired
	private PocketRepository pocketRepository;
	
	@Autowired
	private HitRepository hitRepository;
	
	@Autowired
	private BilliardTableService billiardTableService;
	
	@PostConstruct
	public void setup() {
		
		BilliardTable billiardTable = new BilliardTable();
		
		
	}
	
	

}
