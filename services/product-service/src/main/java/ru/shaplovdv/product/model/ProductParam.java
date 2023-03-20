package ru.shaplovdv.product.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ProductParam {

    List<UUID> idList;
}
