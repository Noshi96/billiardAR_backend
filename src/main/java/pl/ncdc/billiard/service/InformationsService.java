package pl.ncdc.billiard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.ncdc.billiard.models.Ball;
import pl.ncdc.billiard.models.BilliardTable;
import pl.ncdc.billiard.models.Informations;
import pl.ncdc.billiard.models.Pocket;

import java.util.List;

@Service
public class InformationsService {

    private final BilliardTable billiardTable;
    private final HitService hitService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public InformationsService(BilliardTable billiardTable, HitService hitService, SimpMessagingTemplate simpMessagingTemplate) {
        this.billiardTable = billiardTable;
        this.hitService = hitService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Scheduled(fixedRate = 500)
    public void getHitInfo() {
        Ball whiteBall = billiardTable.getWhiteBall();
        Ball selectedBall = billiardTable.getSelectedBall();
        Pocket selectedPocket = billiardTable.getSelectedPocket();
        List<Ball> listBall = billiardTable.getBalls();

        Informations hitInfo = new Informations();
        if (whiteBall != null && selectedBall != null && selectedPocket != null) {
            hitInfo = hitService.getHitInfo(whiteBall.getPoint(), selectedBall.getPoint(), selectedPocket.getPoint(), listBall, selectedPocket.getId());
            hitInfo.setCalculated(true);
        }

        simpMessagingTemplate.convertAndSend("/informations", hitInfo);
    }
}
