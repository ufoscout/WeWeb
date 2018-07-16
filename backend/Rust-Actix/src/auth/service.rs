
use auth::model;
use std::collections::HashMap;

pub trait RolesProvider {

    fn get_all(&self) -> &Vec<model::Role>;

    fn get_by_name(&self, names: &[&str]) -> Vec<&model::Role>;
}

struct InMemoryRolesProvider {
    all_roles: Vec<model::Role>,
    roles_by_name: HashMap<String, model::Role>
}

impl InMemoryRolesProvider {
    pub fn new(all_roles: Vec<model::Role>) -> impl RolesProvider {
        let mut provider = InMemoryRolesProvider{
            all_roles,
            roles_by_name: HashMap::new()
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
mod test {
    use auth::model::Role;
    use super::RolesProvider;

    #[test]
    fn should_return_all_roles() {
        let roles = vec![
            Role{
                id: 0,
                name: "RoleOne".to_string(),
                permissions: vec![]
            },
            Role{
                id: 1,
                name: "RoleTwo".to_string(),
                permissions: vec![]
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
            Role{
                id: 0,
                name: "RoleOne".to_string(),
                permissions: vec![]
            },
            Role{
                id: 1,
                name: "RoleTwo".to_string(),
                permissions: vec![]
            }
        ];
        let provider = super::InMemoryRolesProvider::new(roles.clone());
        let get_by_name = provider.get_by_name(&[]);
        assert!(get_by_name.is_empty());
    }

    #[test]
    fn should_return_role_by_name() {
        let roles = vec![
            Role{
                id: 0,
                name: "RoleOne".to_string(),
                permissions: vec![]
            },
            Role{
                id: 1,
                name: "RoleTwo".to_string(),
                permissions: vec![]
            }
        ];
        let provider = super::InMemoryRolesProvider::new(roles.clone());
        let get_by_name = provider.get_by_name(&["RoleOne"]);
        assert!(!get_by_name.is_empty());
        assert_eq!(1, get_by_name.len());
        assert_eq!("RoleOne", &get_by_name[0].name);
    }

}