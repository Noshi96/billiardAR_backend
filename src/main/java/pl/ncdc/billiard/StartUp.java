package pl.ncdc.billiard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.ncdc.billiard.entity.BilliardTable;
import pl.ncdc.billiard.repository.BallRepository;
import pl.ncdc.billiard.repository.HitRepository;
import pl.ncdc.billiard.repository.PocketRepository;
import pl.ncdc.billiard.service.BilliardTableService;

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
