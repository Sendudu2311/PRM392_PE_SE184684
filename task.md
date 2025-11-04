App title: Your name - student ID (e.g., Nguyen Van A - SE123456)

Create a mobile application that fetches a list of data from a public API, displays it to the user, and provides a robust offline-first experience by caching all data in a local database. The user must also be able to "favorite" items for later viewing.

Screen 1: The "List" Screen (Home Screen) - Database for offline use when needed
​This is the first screen the user sees. It displays the main list of data.
​Data Display: Must display the list of items in a LazyColumn.
​Item UI: Each item in the list must show at least a title and an image (if available from the API).
​Loading State: Must display a loading indicator (e.g., CircularProgressIndicator) while the initial network fetch is in progress.
​Error State: Must display a user-friendly error message (e.g., "Could not load data. Please check your connection.") if the network fetch fails and the local database is empty.
​Navigation: Tapping on any item in the LazyColumn must navigate the user to the "Detail Screen" for that specific item.
Implement a search bar and category filters on the "List" Screen. Categories are listed automatically for selection based on fetched data instead of input manually.

​Screen 2: The "Detail" Screen
​This screen shows the complete details for a single item selected from the List Screen.
​Data Display: Must display all relevant details for the selected item
​Navigation: Must receive the item's unique ID as a navigation argument from the List Screen.
​Favorite Button:
​Must include a "Favorite" button (e.g., an IconButton with a star or heart icon).
​This button must be a "toggle." Tapping it adds the item to the local "Favorites" database. Tapping it again removes it.
​The button's state (e.g., filled vs. outlined icon) must reflect whether the item is currently in the "Favorites" database. This state must update immediately on click.

​Screen 3: The "Favorites" Screen 
​This screen displays only the items the user has marked as a favorite.
​Data Source: This screen's list must be loaded only from the database. It should not make a network call.
​Data Display: Must display the list of favourite items in a LazyColumn (similar to the List Screen).
​Real-time Updates: The list must update automatically if the user:
​Adds a new favorite from the Detail Screen.
​Removes a favorite from the Detail Screen.
​Empty State: Must display a message like "You have no favorites yet" if the favorites database is empty.
Implement a search bar and category filters on the "Favorites" Screen. Categories are listed automatically for selection based on fetched data instead of input manually.


