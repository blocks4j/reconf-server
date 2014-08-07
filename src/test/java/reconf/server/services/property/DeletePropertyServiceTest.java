/*
 *    Copyright 2013-2014 ReConf Team
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package reconf.server.services.property;

import org.junit.*;
import org.mockito.*;
import org.springframework.http.*;
import reconf.server.domain.*;
import reconf.server.repository.*;

public class DeletePropertyServiceTest {

    DeletePropertyService target;
    PropertyRepository properties;

    @Before
    public void before() {
        target = new DeletePropertyService();
        properties = Mockito.mock(PropertyRepository.class);
        target.setProperties(properties);
    }

    @Test
    public void not_found() {
        Mockito.when(properties.exists(Mockito.any(PropertyKey.class))).thenReturn(false);
        Assert.assertTrue(target.doIt("product", "component", "property", "hostname").getStatusCode() == HttpStatus.NOT_FOUND);
        Mockito.verify(properties, Mockito.never()).delete(Mockito.any(PropertyKey.class));
    }

    @Test
    public void found() {
        Mockito.when(properties.exists(Mockito.any(PropertyKey.class))).thenReturn(true);
        Assert.assertTrue(target.doIt("product", "component", "property", "hostname").getStatusCode() == HttpStatus.OK);
        Mockito.verify(properties, Mockito.times(1)).delete(Mockito.any(PropertyKey.class));
    }
}
