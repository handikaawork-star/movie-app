package com.movieapp.core.data.repository

import com.movieapp.core.data.local.FavoriteMovieDao
import com.movieapp.core.data.mapper.toDomain
import com.movieapp.core.data.mapper.toEntity
import com.movieapp.core.data.remote.api.TmdbApiService
import com.movieapp.core.domain.model.Movie
import com.movieapp.core.domain.model.MovieCategory
import com.movieapp.core.domain.model.MovieDetail
import com.movieapp.core.domain.model.Resource
import com.movieapp.core.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class MovieRepositoryImpl(
    private val api: TmdbApiService,
    private val dao: FavoriteMovieDao
) : MovieRepository {

    override fun getMovies(category: MovieCategory): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val response = when (category) {
                MovieCategory.NOW_PLAYING -> api.getNowPlaying()
                MovieCategory.POPULAR -> api.getPopular()
                MovieCategory.TOP_RATED -> api.getTopRated()
            }
            emit(Resource.Success(response.results.map { it.toDomain() }))
        } catch (_: IOException) {
            emit(Resource.Error("Tidak ada koneksi internet"))
        } catch (_: HttpException) {
            emit(Resource.Error("Gagal mengambil data dari server"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getMovieDetail(movieId: Int): Flow<Resource<MovieDetail>> = flow {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(api.getMovieDetail(movieId).toDomain()))
        } catch (_: IOException) {
            emit(Resource.Error("Tidak ada koneksi internet"))
        } catch (_: HttpException) {
            emit(Resource.Error("Gagal mengambil data dari server"))
        }
    }.flowOn(Dispatchers.IO)

    override fun searchMovies(query: String): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(api.searchMovies(query).results.map { it.toDomain() }))
        } catch (_: IOException) {
            emit(Resource.Error("Tidak ada koneksi internet"))
        } catch (_: HttpException) {
            emit(Resource.Error("Gagal mengambil data dari server"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getFavoriteMovies(): Flow<List<Movie>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }.flowOn(Dispatchers.IO)

    override fun isFavorite(movieId: Int): Flow<Boolean> =
        dao.observeById(movieId).map { it != null }.flowOn(Dispatchers.IO)

    override suspend fun setFavorite(movie: Movie, isFavorite: Boolean) {
        if (isFavorite) {
            dao.insert(movie.toEntity())
        } else {
            dao.deleteById(movie.id)
        }
    }
}
