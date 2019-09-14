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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngine;
import com.liferay.portal.kernel.search.SearchEngineHelper;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.users.admin.kernel.util.UsersAdmin;

import it.dontesta.labs.liferay.webservice.customusers.api.CustomUserService;
import it.dontesta.labs.liferay.webservice.customusers.exception.CustomUserServiceException;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Antonio Musarra
 */
@Component(immediate = true, property = {}, service = CustomUserService.class)
public class CustomUserServiceImpl implements CustomUserService {

	@Override
	public List<User> getUsersByCategory(String categoryName)
		throws CustomUserServiceException {

		throw new CustomUserServiceException("To be implements");
	}

	@Override
	public List<User> getUsersByTag(String tagName)
		throws CustomUserServiceException {

		List<User> userList = _searchUsersByTagName(tagName);

		if (Validator.isNotNull(userList) && !userList.isEmpty()) {
			return userList;
		}
		else {
			throw new CustomUserServiceException(
				"Users not found by tag " + tagName);
		}
	}

	@Override
	public List<User> getUsersByTag(String[] tagNames)
		throws CustomUserServiceException {

		throw new CustomUserServiceException("To be implements");
	}

	private List<User> _searchUsersByTagName(String tagName) {
		BooleanQueryImpl usersQuery = new BooleanQueryImpl();
		List<User> result = null;

		SearchEngine searchEngine = _searchEngineHelper.getSearchEngine(
			_searchEngineHelper.getDefaultSearchEngineId());

		SearchContext searchContext = new SearchContext();

		searchContext.setCompanyId(PortalUtil.getDefaultCompanyId());
		searchContext.setEntryClassNames(new String[] {User.class.getName()});
		searchContext.setAndSearch(true);

		usersQuery.addRequiredTerm(
			Field.COMPANY_ID, _portal.getDefaultCompanyId());
		usersQuery.addRequiredTerm(Field.ASSET_TAG_NAMES, tagName);
		usersQuery.addRequiredTerm(Field.STATUS, 0);

		Hits hitsUser = null;

		try {
			hitsUser = searchEngine.getIndexSearcher(
			).search(
				searchContext, usersQuery
			);

			result = _usersAdmin.getUsers(hitsUser);
		}
		catch (SearchException se) {
			if (_log.isErrorEnabled()) {
				_log.error(se);
			}
		}
		catch (PortalException pe) {
			if (_log.isErrorEnabled()) {
				_log.error(pe);
			}
		}

		return result;
	}

	private static Log _log = LogFactoryUtil.getLog(
		CustomUserServiceImpl.class);

	@Reference
	private Portal _portal;

	@Reference
	private SearchEngineHelper _searchEngineHelper;

	@Reference
	private UsersAdmin _usersAdmin;

}