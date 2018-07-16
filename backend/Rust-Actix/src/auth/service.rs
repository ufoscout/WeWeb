use auth::model;
use std::collections::HashMap;

#[derive(Fail, Debug)]
pub enum AuthError {
    #[fail(display = "UnAuthenticatedError")]
    UnAuthenticatedError {},
    #[fail(display = "NoRequiredRole [{}]", role)]
    NoRequiredRole { role: String },
    #[fail(display = "NoRequiredPermission [{}]", permission)]
    NoRequiredPermission { permission: String },
}

pub struct AuthContext {
    auth: model::Auth
}

impl AuthContext {
    pub fn is_authenticated(&self) -> Result<&AuthContext, AuthError> {
        if (self.auth.username.is_empty()) {
            return Err(AuthError::UnAuthenticatedError {});
        };
        Ok(&self)
    }

    pub fn has_role(&self, role: &str) -> Result<&AuthContext, AuthError> {
        self.is_authenticated()?;
        if (!self.has_role_bool(&role)) {
            return Err(AuthError::NoRequiredRole { role: role.to_string() });
        };
        Ok(&self)
    }

    pub fn has_any_role(&self, roles: &[&str]) -> Result<&AuthContext, AuthError> {
        self.is_authenticated()?;
        for role in roles {
            if (self.has_role_bool(*role)) {
                return Ok(&self);
            };
        };
        return Err(AuthError::NoRequiredRole { role: "".to_string() });
    }

    pub fn has_all_roles(&self, roles: &[&str]) -> Result<&AuthContext, AuthError> {
        self.is_authenticated()?;
        for role in roles {
            if (!self.has_role_bool(*role)) {
                return Err(AuthError::NoRequiredRole { role: role.to_string() });
            };
        };
        return Ok(&self);
    }


    fn has_role_bool(&self, role: &str) -> bool {
        self.auth.roles.contains(&role.to_string())
    }
}

pub trait RolesProvider {
    fn get_all(&self) -> &Vec<model::Role>;

    fn get_by_name(&self, names: &[&str]) -> Vec<&model::Role>;
}

struct InMemoryRolesProvider {
    all_roles: Vec<model::Role>,
    roles_by_name: HashMap<String, model::Role>,
}

impl InMemoryRolesProvider {
    pub fn new(all_roles: Vec<model::Role>) -> impl RolesProvider {
        let mut provider = InMemoryRolesProvider {
            all_roles,
            roles_by_name: HashMap::new(),
        };

        for role in &provider.all_roles {
            provider.roles_by_name.insert(role.name.clone(), role.clone());
        }

        provider
    }
}

impl RolesProvider for InMemoryRolesProvider {
    fn get_all(&self) -> &Vec<model::Role> {
        &self.all_roles
    }

    fn get_by_name(&self, names: &[&str]) -> Vec<&model::Role> {
        let mut result = vec![];
        for name in names {
            let roles = self.roles_by_name.get(*name);
            match roles {
                Some(t) => result.push(t),
                None => {}
            }
        };
        result
    }
}

#[cfg(test)]
mod test_role_provider {
    use auth::model::Role;
    use super::RolesProvider;

    #[test]
    fn should_return_all_roles() {
        let roles = vec![
            Role {
                id: 0,
                name: "RoleOne".to_string(),
                permissions: vec![],
            },
            Role {
                id: 1,
                name: "RoleTwo".to_string(),
                permissions: vec![],
            }
        ];
        let provider = super::InMemoryRolesProvider::new(roles.clone());
        let get_all = provider.get_all();
        assert!(!get_all.is_empty());
        assert_eq!(roles.len(), get_all.len());
        assert_eq!(&roles[0].name, &get_all[0].name);
        assert_eq!(&roles[1].name, &get_all[1].name);
    }

    #[test]
    fn should_return_empty_if_no_matching_names() {
        let roles = vec![
            Role {
                id: 0,
                name: "RoleOne".to_string(),
                permissions: vec![],
            },
            Role {
                id: 1,
                name: "RoleTwo".to_string(),
                permissions: vec![],
            }
        ];
        let provider = super::InMemoryRolesProvider::new(roles.clone());
        let get_by_name = provider.get_by_name(&[]);
        assert!(get_by_name.is_empty());
    }

    #[test]
    fn should_return_role_by_name() {
        let roles = vec![
            Role {
                id: 0,
                name: "RoleOne".to_string(),
                permissions: vec![],
            },
            Role {
                id: 1,
                name: "RoleTwo".to_string(),
                permissions: vec![],
            }
        ];
        let provider = super::InMemoryRolesProvider::new(roles.clone());
        let get_by_name = provider.get_by_name(&["RoleOne"]);
        assert!(!get_by_name.is_empty());
        assert_eq!(1, get_by_name.len());
        assert_eq!("RoleOne", &get_by_name[0].name);
    }
}


#[cfg(test)]
mod test_auth_context {
    use auth::model::Auth;
    use super::AuthContext;

    #[test]
    fn should_be_authenticated() {
        let user = Auth {
            id: 0,
            username: "name".to_string(),
            roles: vec![],
        };
        let authContext = super::AuthContext { auth: user };
        assert!(authContext.is_authenticated().is_ok());
    }

    #[test]
    fn should_be_not_authenticated() {
        let user = Auth {
            id: 0,
            username: "".to_string(),
            roles: vec![],
        };
        let authContext = super::AuthContext { auth: user };
        assert!(authContext.is_authenticated().is_err());
    }

    #[test]
    fn should_be_not_authenticated_even_if_has_role() {
        let user = Auth {
            id: 0,
            username: "".to_string(),
            roles: vec!["ADMIN".to_string()],
        };
        let authContext = super::AuthContext { auth: user };
        assert!(authContext.has_role("ADMIN").is_err());
    }

    #[test]
    fn should_have_role() {
        let user = Auth {
            id: 0,
            username: "name".to_string(),
            roles: vec!["ADMIN".to_string()],
        };
        let authContext = super::AuthContext { auth: user };
        assert!(authContext.has_role("ADMIN").is_ok());
    }

    #[test]
    fn should_have_role_2() {
        let user = Auth {
            id: 0,
            username: "name".to_string(),
            roles: vec!["ADMIN".to_string(), "USER".to_string()],
        };
        let authContext = super::AuthContext { auth: user };
        assert!(authContext.has_role("USER").is_ok());
    }

    #[test]
    fn should_not_have_role() {
        let user = Auth {
            id: 0,
            username: "name".to_string(),
            roles: vec!["ADMIN".to_string()],
        };
        let authContext = super::AuthContext { auth: user };
        assert!(authContext.has_role("USER").is_err());
    }

    #[test]
    fn should_have_any_role() {
        let user = Auth {
            id: 0,
            username: "name".to_string(),
            roles: vec!["ADMIN".to_string(), "USER".to_string()],
        };
        let authContext = super::AuthContext { auth: user };
        assert!(authContext.has_any_role(&["USER", "FRIEND"]).is_ok());
    }

    #[test]
    fn should_not_have_any_role() {
        let user = Auth {
            id: 0,
            username: "name".to_string(),
            roles: vec!["ADMIN".to_string(), "OWNER".to_string()],
        };
        let authContext = super::AuthContext { auth: user };
        assert!(authContext.has_any_role(&["USER", "FRIEND"]).is_err());
    }

    #[test]
    fn should_have_all_roles() {
        let user = Auth {
            id: 0,
            username: "name".to_string(),
            roles: vec!["ADMIN".to_string(), "USER".to_string(), "FRIEND".to_string()],
        };
        let authContext = super::AuthContext { auth: user };
        assert!(authContext.has_all_roles(&["USER", "FRIEND"]).is_ok());
    }

    #[test]
    fn should_not_have_all_roles() {
        let user = Auth {
            id: 0,
            username: "name".to_string(),
            roles: vec!["ADMIN".to_string(), "USER".to_string()],
        };
        let authContext = super::AuthContext { auth: user };
        assert!(authContext.has_all_roles(&["USER", "FRIEND"]).is_err());
    }
}