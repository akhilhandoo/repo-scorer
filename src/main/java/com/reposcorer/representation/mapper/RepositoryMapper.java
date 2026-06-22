package com.reposcorer.representation.mapper;

import com.reposcorer.config.MapStructConfig;
import com.reposcorer.remote.model.Repository;
import com.reposcorer.representation.Repo;
import org.mapstruct.*;

@Mapper(config = MapStructConfig.class)
public interface RepositoryMapper {
  Repo fromRepository(Repository repository);
}
