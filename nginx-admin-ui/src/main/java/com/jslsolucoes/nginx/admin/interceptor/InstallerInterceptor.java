/*******************************************************************************
 * Copyright 2016 JSL Solucoes LTDA - https://jslsolucoes.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.jslsolucoes.nginx.admin.interceptor;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.HibernateException;

import com.jslsolucoes.nginx.admin.annotation.CheckForInstaller;
import com.jslsolucoes.nginx.admin.controller.InstallerController;
import com.jslsolucoes.nginx.admin.repository.UserRepository;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.AcceptsWithAnnotations;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;

@RequestScoped
@Intercepts(after=AuthenticationInterceptor.class)
@AcceptsWithAnnotations(CheckForInstaller.class)
public class InstallerInterceptor {

	@Inject
	private Result result;

	@Inject
	private UserRepository userRepository;

	@AroundCall
	public void intercept(SimpleInterceptorStack stack) throws HibernateException, IOException {
		if (CollectionUtils.isEmpty(userRepository.listAll())) {
			this.result.redirectTo(InstallerController.class).form();
		} else {
			stack.next();
		}
	}

}