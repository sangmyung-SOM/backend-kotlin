package com.smu.som.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.SingletonSupport
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

//ObjectMapper에 대한 setting
@Configuration
class ObjectMapperConfig {
	//bean 등록
	@Bean
	fun objectMapper(): ObjectMapper {
		return ObjectMapper().registerModule(KotlinModule.Builder()
			.withReflectionCacheSize(512)
			.configure(KotlinFeature.NullToEmptyCollection, false)
			.configure(KotlinFeature.NullToEmptyMap, false)
			.configure(KotlinFeature.NullIsSameAsDefault, false)
			.configure(KotlinFeature.StrictNullChecks, false)
			.build());
	}
}
