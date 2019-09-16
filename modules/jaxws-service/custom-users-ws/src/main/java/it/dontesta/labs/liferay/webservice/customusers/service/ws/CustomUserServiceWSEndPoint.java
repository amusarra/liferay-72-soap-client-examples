/*
 * Copyright (c) 2019 Antonio Musarra's Blog - https://www.dontesta.it
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package it.dontesta.labs.liferay.webservice.customusers.service.ws;

import it.dontesta.labs.liferay.webservice.customusers.api.PersonService;
import it.dontesta.labs.liferay.webservice.customusers.exception.CustomUserServiceException;
import it.dontesta.labs.liferay.webservice.customusers.model.PersonList;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author antoniomusarra
 */
@Component(
	immediate = true, property = "custom.user.jax.ws.service=true",
	service = CustomUserServiceWSEndPoint.class
)
@WebService
public class CustomUserServiceWSEndPoint implements PersonService {

	@Override
	@WebMethod
	public PersonList getUsersByCategory(String categoryName)
		throws CustomUserServiceException {

		return _personService.getUsersByCategory(categoryName);
	}

	@Override
	@WebMethod
	public PersonList getUsersByTag(String tagName)
		throws CustomUserServiceException {

		return _personService.getUsersByTag(tagName);
	}

	@Override
	@WebMethod(exclude = true)
	public PersonList getUsersByTag(String[] tagNames)
		throws CustomUserServiceException {

		return _personService.getUsersByTag(tagNames);
	}

	@WebMethod
	public PersonList getUsersByTags(String[] tagNames)
		throws CustomUserServiceException {

		return _personService.getUsersByTag(tagNames);
	}

	@Reference
	private PersonService _personService;

}