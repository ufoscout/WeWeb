/*******************************************************************************
 * Copyright 2017 Francesco Cina'

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.weweb.core.config

import com.ufoscout.coreutils.jwt.JwtConfig
import com.ufoscout.properlty.Properlty
import com.ufoscout.vertk.kodein.web.RouterConfig

data class CoreConfig(val routerConfig: RouterConfig,
                      val jwt: JwtConfig ) {

    companion object {
        fun build(prop: Properlty) : CoreConfig {
            return CoreConfig(
                    RouterConfig(prop.getInt("server.port")!!),
                    JwtConfig(
                            prop.get("jwt.secret")!!,
                            prop.get("jwt.signatureAlgorithm")!!,
                            prop.getLong("jwt.tokenValidityMinutes")!!
                    )
            )
        }
    }

}
