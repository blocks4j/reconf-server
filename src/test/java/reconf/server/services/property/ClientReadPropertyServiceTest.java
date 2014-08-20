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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static reconf.server.domain.fixture.RestDataFixture.*;
import java.util.*;
import org.junit.*;
import org.mockito.*;
import org.springframework.test.web.servlet.*;
import reconf.server.*;
import reconf.server.domain.*;
import reconf.server.repository.*;
import reconf.server.services.*;

public class ClientReadPropertyServiceTest {

    MockMvc mockMvc;

    @InjectMocks
    ClientReadPropertyService service;

    @Mock
    PropertyRepository propertyRepository;

    @Mock
    JavaScriptEngine jsEngine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = standaloneSetup(service).build();
    }

    @Test
    public void found() throws Exception {
        Property property = standardProperty();

        when(propertyRepository.findByKeyProductAndKeyComponentAndKeyNameOrderByRulePriorityDescKeyRuleNameAsc(PROPERTY_KEY_PRODUCT, PROPERTY_KEY_COMPONENT, PROPERTY_KEY_NAME))
            .thenReturn(Collections.singletonList(property));

        when(jsEngine.eval(Mockito.anyString(), Mockito.anyMap(), Mockito.anyString())).thenReturn(true);

        this.mockMvc.perform(get("/{prod}/{comp}/{prop}", PROPERTY_KEY_PRODUCT, PROPERTY_KEY_COMPONENT, PROPERTY_KEY_NAME)
            .accept(ReConfConstants.MT_PROTOCOL_V1))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(property.getValue()));

        verify(propertyRepository).findByKeyProductAndKeyComponentAndKeyNameOrderByRulePriorityDescKeyRuleNameAsc(PROPERTY_KEY_PRODUCT, PROPERTY_KEY_COMPONENT, PROPERTY_KEY_NAME);
    }

    @Test
    public void not_found() throws Exception {
        when(propertyRepository.findByKeyProductAndKeyComponentAndKeyNameOrderByRulePriorityDescKeyRuleNameAsc(PROPERTY_KEY_PRODUCT, PROPERTY_KEY_COMPONENT, PROPERTY_KEY_NAME))
        .thenReturn(Collections.EMPTY_LIST);

        this.mockMvc.perform(get("/{prod}/{comp}/{prop}", PROPERTY_KEY_PRODUCT, PROPERTY_KEY_COMPONENT, PROPERTY_KEY_NAME)
            .accept(ReConfConstants.MT_PROTOCOL_V1))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(propertyRepository).findByKeyProductAndKeyComponentAndKeyNameOrderByRulePriorityDescKeyRuleNameAsc(PROPERTY_KEY_PRODUCT, PROPERTY_KEY_COMPONENT, PROPERTY_KEY_NAME);
    }
}
