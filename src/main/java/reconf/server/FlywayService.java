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
package reconf.server;

import javax.sql.*;
import org.flywaydb.core.*;

/*
 * Flyway must be called manually
 * SpringSecurity starts BEFORE Flyway
 * This causes errors during because SpringSecurity tables are created after SpringSecurity startup
 */
public class FlywayService {

    public void setUpDB(DataSource ds) {
        try {
            Flyway flyway = new Flyway();
            flyway.setInitOnMigrate(true);
            flyway.setSqlMigrationPrefix("V");
            flyway.setSqlMigrationSuffix(".sql");
            flyway.setInitVersion("1");
            flyway.setLocations("./");
            flyway.setDataSource(ds);
            flyway.migrate();
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
