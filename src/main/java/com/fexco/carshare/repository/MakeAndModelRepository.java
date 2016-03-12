package com.fexco.carshare.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.fexco.carshare.domain.MakeAndModel;

public interface MakeAndModelRepository extends CrudRepository<MakeAndModel, Long>{
	
	List<MakeAndModel> findByMakeId(Long makeId);
	MakeAndModel findByMakeIdAndModel(Long makeId, String model);
}
