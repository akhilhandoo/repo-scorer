package com.reposcorer.config;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;

@MapperConfig(componentModel = SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public class MapStructConfig {}
