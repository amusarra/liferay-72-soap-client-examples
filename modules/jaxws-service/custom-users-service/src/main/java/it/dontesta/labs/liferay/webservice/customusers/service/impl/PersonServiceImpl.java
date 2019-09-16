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

package it.dontesta.labs.liferay.webservice.customusers.service.impl;

import com.liferay.portal.kernel.model.User;

import it.dontesta.labs.liferay.webservice.customusers.api.CustomUserService;
import it.dontesta.labs.liferay.webservice.customusers.api.PersonService;
import it.dontesta.labs.liferay.webservice.customusers.exception.CustomUserServiceException;
import it.dontesta.labs.liferay.webservice.customusers.model.Person;
import it.dontesta.labs.liferay.webservice.customusers.model.PersonList;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Antonio Musarra
 */
@Component(immediate = true, property = {}, service = PersonService.class)
public class PersonServiceImpl implements PersonService {

	@Override
	public PersonList getUsersByCategory(String categoryName)
		throws CustomUserServiceException {

		throw new CustomUserServiceException("To be implements");
	}

	@Override
	public PersonList getUsersByTag(String tagName)
		throws CustomUserServiceException {

		List<User> userList = _customUserService.getUsersByTag(tagName);
		PersonList personList = new PersonList();

		for (User user : userList) {
			Person person = new Person();

			person.setUserId(user.getUserId());
			person.setUserName(user.getScreenName());
			person.setEmail(user.getEmailAddress());
			person.setFirstName(user.getFirstName());
			person.setLastName(user.getLastName());

			personList.getPersonList(
			).add(
				person
			);
		}

		return personList;
	}

	@Override
	public PersonList getUsersByTag(String[] tagNames)
		throws CustomUserServiceException {

		throw new CustomUserServiceException("To be implements");
	}

	@Reference
	private CustomUserService _customUserService;

}