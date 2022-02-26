package com.intentBi.slackDemo.controller;

import com.intentBi.slackDemo.entity.request.ReportRequest;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@RestController
public class Controller {
    @PostMapping("/getimage")
    public byte[] getImage(@RequestBody ReportRequest request){

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            BufferedImage bImage= ImageIO.read(new File("/Users/Prakash/Downloads/293040.png"));
            ImageIO.write(bImage,"png",bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    @GetMapping("/service")
    public String serviceMethod () {
        return "Service Successfully Established";
    }

}
