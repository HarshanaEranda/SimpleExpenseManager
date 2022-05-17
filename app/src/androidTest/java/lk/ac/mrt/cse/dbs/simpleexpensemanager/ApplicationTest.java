/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;


import android.content.Context;
import android.widget.DatePicker;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManger;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


import static org.junit.Assert.assertTrue;

public class ApplicationTest {
    private static ExpenseManager expenseManager;


    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        expenseManager = new PersistentExpenseManger(context);


    }

    @Test
    public void TestAddAccount() {
    expenseManager.addAccount("1234","Nitambuwa","Harshana",1234);
   assertTrue(expenseManager.getAccountNumbersList().contains("1234"));

    }
    @Test
    public void TestTransaction() throws InvalidAccountException {
       int NumberOfPreviousTransactions=(expenseManager.getTransactionLogs().size());
       expenseManager.updateAccountBalance("1234",11,05,2022, ExpenseType.valueOf("INCOME"),"12348");
        assertTrue((expenseManager.getTransactionLogs().size() == (NumberOfPreviousTransactions+1)));
    }
}