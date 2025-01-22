use dioxus::prelude::*;

#[server(UserSession)]
pub async fn session(input: String) -> Result<String, ServerFnError> {
    let session: tower_sessions::Session = extract_session().await?; 
    Ok(input)
}

const USER_SESSION_DATA_KEY: &str = "user_session_data_key";

#[derive(Default, serde::Deserialize, serde::Serialize)]
pub struct UserSessionData {
    pub user_id: i32,
}

impl UserSessionData {
    pub fn new(user_id: i32) -> Self {
        Self { user_id }
    }

    /// Fetchs the user session data from the session data
    pub async fn fetch() -> Result<Option<Self>, ServerFnError> {
        extract_user_session().await
    }

    /// Sets the user session data in the session data
    pub async fn set(&self) -> Result<(), ServerFnError> {
        set_user_session(&self).await
    }

    /// Removes the user session data from the session data
    pub async fn remove() -> Result<Option<Self>, ServerFnError> {
        remove_user_session().await
    }
    
}

#[inline]
pub async fn extract_user_session() -> Result<Option<UserSessionData>, ServerFnError> {
    let session: tower_sessions::Session = extract_session().await?;
    let data = session.get::<UserSessionData>(USER_SESSION_DATA_KEY).await?;
    Ok(data)
}

#[inline]
pub async fn set_user_session(data: &UserSessionData) -> Result<(), ServerFnError> {
    let session: tower_sessions::Session = extract_session().await?;
    session.insert(USER_SESSION_DATA_KEY, data).await?;
    Ok(())
}

#[inline]
pub async fn remove_user_session() -> Result<Option<UserSessionData>, ServerFnError> {
    let session: tower_sessions::Session = extract_session().await?;
    let data = session.remove(USER_SESSION_DATA_KEY).await?;
    Ok(data)
}

#[inline]
pub async fn extract_session() -> Result<tower_sessions::Session, ServerFnError> {
    extract::<tower_sessions::Session, _>()
        .await
        .map_err(|_| ServerFnError::new("SessionLayer was not found"))
}