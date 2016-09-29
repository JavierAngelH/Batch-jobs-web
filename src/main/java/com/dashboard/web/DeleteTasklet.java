package com.dashboard.web;

import javax.annotation.Resource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.dashboard.service.ConexionDAO;

@Component("deleteTasklet")
public class DeleteTasklet implements Tasklet{

	@Resource
	ConexionDAO conexionDao;
	
	private void deleteOldInfo(){
		conexionDao.deleteOldRowsCliente();
		conexionDao.deleteOldRowsAplicacion();
	}
	
	
	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1)
			throws Exception {

		System.out.println("Comienza borrar informacion");
		deleteOldInfo();
		System.out.println("Culmina borrar informacion");

		
		
		return RepeatStatus.FINISHED;
	}

}
