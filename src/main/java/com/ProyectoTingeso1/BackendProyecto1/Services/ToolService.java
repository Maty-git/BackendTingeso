package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolService {
    @Autowired
    ToolRepository toolRepository;
    
    public boolean saveTool(Tool tool) {
        int quantity =  tool.getQuantity();
        int i;
        for( i = 0; i < quantity; i++ ) {
            Tool newTool = new Tool();
            newTool.setName(tool.getName());
            newTool.setCategory(tool.getCategory());
            newTool.setState(tool.getState());
            newTool.setQuantity(1);
            newTool.setRentDailyRate(tool.getRentDailyRate());
            newTool.setLateFee(tool.getLateFee());
            newTool.setReplacementValue(tool.getReplacementValue());
            toolRepository.save(newTool);
        }
        if(i == quantity){
            return true;
        }else{
            return false;
        }
    }
    public List<Tool> getAllTools() {
        return toolRepository.findAll();
    }
}
