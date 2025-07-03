package com.alistermcconnell.ticketsystem.dto;

import com.alistermcconnell.ticketsystem.model.Status;
import lombok.Data;

@Data
public class UpdateStatusRequest {

    private Status status;
}
